<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="统计日期 yyyymmdd" prop="statDate">
        <el-input
          v-model="queryParams.statDate"
          placeholder="请输入统计日期 yyyymmdd"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当日提现手续费总额" prop="totalFee">
        <el-input
          v-model="queryParams.totalFee"
          placeholder="请输入当日提现手续费总额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际分红总额" prop="distributedFee">
        <el-input
          v-model="queryParams.distributedFee"
          placeholder="请输入实际分红总额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="符合条件的V9用户数" prop="userCount">
        <el-input
          v-model="queryParams.userCount"
          placeholder="请输入符合条件的V9用户数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="人均分得金额" prop="perUserAmount">
        <el-input
          v-model="queryParams.perUserAmount"
          placeholder="请输入人均分得金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="未分红原因" prop="failReason">
        <el-input
          v-model="queryParams.failReason"
          placeholder="请输入未分红原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xms:withdrawFeeShareStatDay:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['xms:withdrawFeeShareStatDay:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xms:withdrawFeeShareStatDay:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:withdrawFeeShareStatDay:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="withdrawFeeShareStatDayList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="订单号" prop="orderNo" />
      <el-table-column align="center" label="当日提现手续费总额" prop="totalFee" />
      <el-table-column align="center" label="实际分红总额" prop="distributedFee" />
      <el-table-column align="center" label="符合条件的V9用户数" prop="userCount" />
      <el-table-column align="center" label="人均分得金额" prop="perUserAmount" />
<!--      <el-table-column label="参与分红快照" align="center" prop="shareUserSnapshot" />-->
      <el-table-column align="center" label="未分红原因" prop="failReason" />
      <el-table-column align="center" label="分红时间点" prop="statDate" />
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:withdrawFeeShareStatDay:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:withdrawFeeShareStatDay:remove']"
          >删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改提现手续费分红对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="统计日期 yyyymmdd" prop="statDate">
          <el-input v-model="form.statDate" placeholder="请输入统计日期 yyyymmdd" />
        </el-form-item>
        <el-form-item label="当日提现手续费总额" prop="totalFee">
          <el-input v-model="form.totalFee" placeholder="请输入当日提现手续费总额" />
        </el-form-item>
        <el-form-item label="实际分红总额" prop="distributedFee">
          <el-input v-model="form.distributedFee" placeholder="请输入实际分红总额" />
        </el-form-item>
        <el-form-item label="符合条件的V9用户数" prop="userCount">
          <el-input v-model="form.userCount" placeholder="请输入符合条件的V9用户数" />
        </el-form-item>
        <el-form-item label="人均分得金额" prop="perUserAmount">
          <el-input v-model="form.perUserAmount" placeholder="请输入人均分得金额" />
        </el-form-item>
        <el-form-item label="参与分红快照 userId#account,逗号分隔" prop="shareUserSnapshot">
          <el-input v-model="form.shareUserSnapshot" placeholder="请输入内容" type="textarea" />
        </el-form-item>
        <el-form-item label="未分红原因" prop="failReason">
          <el-input v-model="form.failReason" placeholder="请输入未分红原因" />
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
import { listWithdrawFeeShareStatDay, getWithdrawFeeShareStatDay, delWithdrawFeeShareStatDay, addWithdrawFeeShareStatDay, updateWithdrawFeeShareStatDay } from "@/api/xms/withdrawFeeShareStatDay";

export default {
  name: "WithdrawFeeShareStatDay",
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
      // 提现手续费分红表格数据
      withdrawFeeShareStatDayList: [],
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
        orderNo: null,
        statDate: null,
        totalFee: null,
        distributedFee: null,
        userCount: null,
        perUserAmount: null,
        shareUserSnapshot: null,
        failReason: null,
        createTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        statDate: [
          { required: true, message: "统计日期 yyyymmdd不能为空", trigger: "blur" }
        ],
        totalFee: [
          { required: true, message: "当日提现手续费总额不能为空", trigger: "blur" }
        ],
        distributedFee: [
          { required: true, message: "实际分红总额不能为空", trigger: "blur" }
        ],
        userCount: [
          { required: true, message: "符合条件的V9用户数不能为空", trigger: "blur" }
        ],
        perUserAmount: [
          { required: true, message: "人均分得金额不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询提现手续费分红列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listWithdrawFeeShareStatDay(this.queryParams).then(response => {
        this.withdrawFeeShareStatDayList = response.rows;
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
        orderNo: null,
        statDate: null,
        totalFee: null,
        distributedFee: null,
        userCount: null,
        perUserAmount: null,
        shareUserSnapshot: null,
        failReason: null,
        remark: null,
        createTime: null
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
      this.title = "添加提现手续费分红";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWithdrawFeeShareStatDay(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改提现手续费分红";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWithdrawFeeShareStatDay(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWithdrawFeeShareStatDay(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除提现手续费分红编号为"' + ids + '"的数据项？').then(function() {
        return delWithdrawFeeShareStatDay(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/withdrawFeeShareStatDay/export', {
        ...this.queryParams
      }, `withdrawFeeShareStatDay_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
