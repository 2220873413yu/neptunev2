<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="池子类型：1:矿池,2:消费分红池,3:手续费分红池" prop="poolType">
        <el-select v-model="queryParams.poolType" clearable placeholder="请选择池子类型：1:矿池,2:消费分红池,3:手续费分红池">
          <el-option
            v-for="dict in dict.type.t_reward_pool_config_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分红批次号，如20251205-01" prop="batchNo">
        <el-input
          v-model="queryParams.batchNo"
          clearable
          placeholder="请输入分红批次号，如20251205-01"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分红日期" prop="batchDate">
        <el-date-picker v-model="queryParams.batchDate"
          clearable
          placeholder="请选择分红日期"
          type="date"
          value-format="yyyy-MM-dd">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="本批次总金额" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          clearable
          placeholder="请输入本批次总金额"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="静态部分金额" prop="staticAmount">
        <el-input
          v-model="queryParams.staticAmount"
          clearable
          placeholder="请输入静态部分金额"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="动态部分金额" prop="dynamicAmount">
        <el-input
          v-model="queryParams.dynamicAmount"
          clearable
          placeholder="请输入动态部分金额"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="daterange"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:rewardPoolBatch:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:rewardPoolBatch:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:rewardPoolBatch:remove']"
          :disabled="multiple"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:rewardPoolBatch:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rewardPoolBatchList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column align="center" label="批次ID" prop="id" />
      <el-table-column align="center" label="池子类型：1:矿池,2:消费分红池,3:手续费分红池" prop="poolType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_reward_pool_config_type" :value="scope.row.poolType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分红批次号，如20251205-01" prop="batchNo" />
      <el-table-column align="center" label="分红日期" prop="batchDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.batchDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="本批次总金额" prop="totalAmount" />
      <el-table-column align="center" label="静态部分金额" prop="staticAmount" />
      <el-table-column align="center" label="动态部分金额" prop="dynamicAmount" />
      <el-table-column align="center" label="状态：0-待分配 1-分配中 2-已完成 3-失败" prop="status" />
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:rewardPoolBatch:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['xms:rewardPoolBatch:remove']"
            icon="el-icon-delete"
            size="mini"
            type="text"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改分红批次记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="池子类型：1:矿池,2:消费分红池,3:手续费分红池" prop="poolType">
          <el-select v-model="form.poolType" placeholder="请选择池子类型：1:矿池,2:消费分红池,3:手续费分红池">
            <el-option
              v-for="dict in dict.type.t_reward_pool_config_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分红批次号，如20251205-01" prop="batchNo">
          <el-input v-model="form.batchNo" placeholder="请输入分红批次号，如20251205-01" />
        </el-form-item>
        <el-form-item label="分红日期" prop="batchDate">
          <el-date-picker v-model="form.batchDate"
            clearable
            placeholder="请选择分红日期"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="本批次总金额" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入本批次总金额" />
        </el-form-item>
        <el-form-item label="静态部分金额" prop="staticAmount">
          <el-input v-model="form.staticAmount" placeholder="请输入静态部分金额" />
        </el-form-item>
        <el-form-item label="动态部分金额" prop="dynamicAmount">
          <el-input v-model="form.dynamicAmount" placeholder="请输入动态部分金额" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRewardPoolBatch, getRewardPoolBatch, delRewardPoolBatch, addRewardPoolBatch, updateRewardPoolBatch } from "@/api/xms/rewardPoolBatch";

export default {
  name: "RewardPoolBatch",
  dicts: ['t_reward_pool_config_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 分红批次记录表格数据
      rewardPoolBatchList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        poolType: null,
        batchNo: null,
        batchDate: null,
        totalAmount: null,
        staticAmount: null,
        dynamicAmount: null,
        status: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        poolType: [
          { required: true, message: "池子类型：1:矿池,2:消费分红池,3:手续费分红池不能为空", trigger: "change" }
        ],
        batchNo: [
          { required: true, message: "分红批次号，如20251205-01不能为空", trigger: "blur" }
        ],
        batchDate: [
          { required: true, message: "分红日期不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "本批次总金额不能为空", trigger: "blur" }
        ],
        staticAmount: [
          { required: true, message: "静态部分金额不能为空", trigger: "blur" }
        ],
        dynamicAmount: [
          { required: true, message: "动态部分金额不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态：0-待分配 1-分配中 2-已完成 3-失败不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询分红批次记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listRewardPoolBatch(this.queryParams).then(response => {
        this.rewardPoolBatchList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        poolType: null,
        batchNo: null,
        batchDate: null,
        totalAmount: null,
        staticAmount: null,
        dynamicAmount: null,
        status: null,
        remark: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加分红批次记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRewardPoolBatch(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改分红批次记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRewardPoolBatch(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRewardPoolBatch(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除分红批次记录编号为"' + ids + '"的数据项？').then(function() {
        return delRewardPoolBatch(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/rewardPoolBatch/export', {
        ...this.queryParams
      }, `rewardPoolBatch_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
