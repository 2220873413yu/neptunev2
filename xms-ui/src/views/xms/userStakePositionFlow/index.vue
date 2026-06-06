<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="流水号" prop="flowNo">
        <el-input
          v-model="queryParams.flowNo"
          placeholder="请输入流水号(唯一)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联业务单号)" prop="bizOrderNo">
        <el-input
          v-model="queryParams.bizOrderNo"
          placeholder="请输入关联业务单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="变动金额(正加负减)" prop="changeAmount">
        <el-input
          v-model="queryParams.changeAmount"
          placeholder="请输入变动金额(正加负减)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动前总质押" prop="beforeTotalStake">
        <el-input
          v-model="queryParams.beforeTotalStake"
          placeholder="请输入变动前总质押"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动后总质押" prop="afterTotalStake">
        <el-input
          v-model="queryParams.afterTotalStake"
          placeholder="请输入变动后总质押"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="轮次ID" prop="stakeRoundId">
        <el-input
          v-model="queryParams.stakeRoundId"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入轮次ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
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
          v-hasPermi="['xms:userStakePositionFlow:add']"
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
          v-hasPermi="['xms:userStakePositionFlow:edit']"
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
          v-hasPermi="['xms:userStakePositionFlow:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userStakePositionFlow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userStakePositionFlowList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="流水号" align="center" prop="flowNo" />
      <el-table-column label="轮次ID" align="center" prop="stakeRoundId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
<!--      <el-table-column label="变动类型:STAKE/UNSTAKE/ADJUST/FREEZE/UNFREEZE" align="center" prop="changeType" />-->
      <el-table-column label="来源订单号" align="center" prop="bizOrderNo" />
      <el-table-column label="变动金额" align="center" prop="changeAmount" />
      <el-table-column label="变动前总质押" align="center" prop="beforeTotalStake" />
      <el-table-column label="变动后总质押" align="center" prop="afterTotalStake" />
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
            v-hasPermi="['xms:userStakePositionFlow:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userStakePositionFlow:remove']"
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

    <!-- 添加或修改用户持仓变动流水对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流水号(唯一)" prop="flowNo">
          <el-input v-model="form.flowNo" placeholder="请输入流水号(唯一)" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="关联业务单号(如质押订单号)" prop="bizOrderNo">
          <el-input v-model="form.bizOrderNo" placeholder="请输入关联业务单号(如质押订单号)" />
        </el-form-item>
        <el-form-item label="变动金额(正加负减)" prop="changeAmount">
          <el-input v-model="form.changeAmount" placeholder="请输入变动金额(正加负减)" />
        </el-form-item>
        <el-form-item label="变动前总质押" prop="beforeTotalStake">
          <el-input v-model="form.beforeTotalStake" placeholder="请输入变动前总质押" />
        </el-form-item>
        <el-form-item label="变动后总质押" prop="afterTotalStake">
          <el-input v-model="form.afterTotalStake" placeholder="请输入变动后总质押" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="轮次表id" prop="stakeRoundId">
          <el-input v-model="form.stakeRoundId" placeholder="请输入轮次表id" />
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
import { listUserStakePositionFlow, getUserStakePositionFlow, delUserStakePositionFlow, addUserStakePositionFlow, updateUserStakePositionFlow } from "@/api/xms/userStakePositionFlow";

export default {
  name: "UserStakePositionFlow",
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
      // 用户持仓变动流水表格数据
      userStakePositionFlowList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 轮次表id时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        flowNo: null,
        userId: null,
        changeType: null,
        bizOrderNo: null,
        changeAmount: null,
        beforeTotalStake: null,
        afterTotalStake: null,
        createTime: null,
        stakeRoundId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        flowNo: [
          { required: true, message: "流水号(唯一)不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        changeType: [
          { required: true, message: "变动类型:STAKE/UNSTAKE/ADJUST/FREEZE/UNFREEZE不能为空", trigger: "change" }
        ],
        changeAmount: [
          { required: true, message: "变动金额(正加负减)不能为空", trigger: "blur" }
        ],
        beforeTotalStake: [
          { required: true, message: "变动前总质押不能为空", trigger: "blur" }
        ],
        afterTotalStake: [
          { required: true, message: "变动后总质押不能为空", trigger: "blur" }
        ],
        stakeRoundId: [
          { required: true, message: "轮次表id不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户持仓变动流水列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listUserStakePositionFlow(this.queryParams).then(response => {
        this.userStakePositionFlowList = response.rows;
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
        flowNo: null,
        userId: null,
        changeType: null,
        bizOrderNo: null,
        changeAmount: null,
        beforeTotalStake: null,
        afterTotalStake: null,
        remark: null,
        createTime: null,
        stakeRoundId: null
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
      this.title = "添加用户持仓变动流水";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserStakePositionFlow(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户持仓变动流水";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserStakePositionFlow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserStakePositionFlow(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户持仓变动流水编号为"' + ids + '"的数据项？').then(function() {
        return delUserStakePositionFlow(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userStakePositionFlow/export', {
        ...this.queryParams
      }, `userStakePositionFlow_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
