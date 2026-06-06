<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId" label-width="120px">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阶段" prop="segNo" label-width="120px">
        <el-input
          v-model="queryParams.segNo"
          placeholder="请输入阶段 1-5"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源类型" prop="sourceType" label-width="120px">
        <el-select v-model="queryParams.sourceType" placeholder="请选择来源类型 1:静态提现 2:动态提现 3:其他" clearable>
          <el-option
            v-for="dict in dict.type.t_user_wealth_vault_flow_source_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="来源订单号" prop="sourceOrderNo" label-width="120px">
        <el-input
          v-model="queryParams.sourceOrderNo"
          placeholder="请输入来源订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="变动金额" prop="changeAmount">
        <el-input
          v-model="queryParams.changeAmount"
          placeholder="请输入变动金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动前该段余额" prop="beforeAmount">
        <el-input
          v-model="queryParams.beforeAmount"
          placeholder="请输入变动前该段余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动后该段余额" prop="afterAmount">
        <el-input
          v-model="queryParams.afterAmount"
          placeholder="请输入变动后该段余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="触发时价格" prop="triggerPrice">
        <el-input
          v-model="queryParams.triggerPrice"
          placeholder="请输入触发时价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['xms:userWealthVaultFlow:add']"
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
          v-hasPermi="['xms:userWealthVaultFlow:edit']"
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
          v-hasPermi="['xms:userWealthVaultFlow:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userWealthVaultFlow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userWealthVaultFlowList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="阶段" align="center" prop="segNo" />
      <el-table-column label="来源类型" align="center" prop="sourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_wealth_vault_flow_source_type" :value="scope.row.sourceType"/>
        </template>
      </el-table-column>
      <el-table-column label="来源订单号" align="center" prop="sourceOrderNo" />
      <el-table-column label="变动金额" align="center" prop="changeAmount" />
      <el-table-column label="变动前该段余额" align="center" prop="beforeAmount" />
      <el-table-column label="变动后该段余额" align="center" prop="afterAmount" />
      <el-table-column label="触发时价格" align="center" prop="triggerPrice" />
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
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
            v-hasPermi="['xms:userWealthVaultFlow:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userWealthVaultFlow:remove']"
          >删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户财富仓流水对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="阶段 1-5" prop="segNo">
          <el-input v-model="form.segNo" placeholder="请输入阶段 1-5" />
        </el-form-item>
        <el-form-item label="来源类型 1:静态提现 2:动态提现 3:其他" prop="sourceType">
          <el-select v-model="form.sourceType" placeholder="请选择来源类型 1:静态提现 2:动态提现 3:其他">
            <el-option
              v-for="dict in dict.type.t_user_wealth_vault_flow_source_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="来源订单号" prop="sourceOrderNo">
          <el-input v-model="form.sourceOrderNo" placeholder="请输入来源订单号" />
        </el-form-item>
        <el-form-item label="变动金额" prop="changeAmount">
          <el-input v-model="form.changeAmount" placeholder="请输入变动金额" />
        </el-form-item>
        <el-form-item label="变动前该段余额" prop="beforeAmount">
          <el-input v-model="form.beforeAmount" placeholder="请输入变动前该段余额" />
        </el-form-item>
        <el-form-item label="变动后该段余额" prop="afterAmount">
          <el-input v-model="form.afterAmount" placeholder="请输入变动后该段余额" />
        </el-form-item>
        <el-form-item label="触发时价格" prop="triggerPrice">
          <el-input v-model="form.triggerPrice" placeholder="请输入触发时价格" />
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
import { listUserWealthVaultFlow, getUserWealthVaultFlow, delUserWealthVaultFlow, addUserWealthVaultFlow, updateUserWealthVaultFlow } from "@/api/xms/userWealthVaultFlow";

export default {
  name: "UserWealthVaultFlow",
  dicts: ['t_user_wealth_vault_flow_source_type'],
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
      // 用户财富仓流水表格数据
      userWealthVaultFlowList: [],
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
        userId: null,
        segNo: null,
        sourceType: null,
        sourceOrderNo: null,
        changeAmount: null,
        beforeAmount: null,
        afterAmount: null,
        triggerPrice: null,
        createTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        segNo: [
          { required: true, message: "阶段 1-5不能为空", trigger: "blur" }
        ],
        changeAmount: [
          { required: true, message: "变动金额不能为空", trigger: "blur" }
        ],
        beforeAmount: [
          { required: true, message: "变动前该段余额不能为空", trigger: "blur" }
        ],
        afterAmount: [
          { required: true, message: "变动后该段余额不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户财富仓流水列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listUserWealthVaultFlow(this.queryParams).then(response => {
        this.userWealthVaultFlowList = response.rows;
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
        userId: null,
        segNo: null,
        sourceType: null,
        sourceOrderNo: null,
        changeAmount: null,
        beforeAmount: null,
        afterAmount: null,
        triggerPrice: null,
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
      this.title = "添加用户财富仓流水";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserWealthVaultFlow(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户财富仓流水";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserWealthVaultFlow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserWealthVaultFlow(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户财富仓流水编号为"' + ids + '"的数据项？').then(function() {
        return delUserWealthVaultFlow(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userWealthVaultFlow/export', {
        ...this.queryParams
      }, `userWealthVaultFlow_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
