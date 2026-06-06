<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="用户UID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户UID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <el-form-item label="用户账号" label-width="100px" prop="userAccount">
        <el-input
          v-model="queryParams.userAccount"
          clearable
          placeholder="请输入用户账号"
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['xms:userIncomeSummary:add']"
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
          v-hasPermi="['xms:userIncomeSummary:edit']"
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
          v-hasPermi="['xms:userIncomeSummary:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:userIncomeSummary:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userIncomeSummaryList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="用户ID" prop="userId"/>
      <el-table-column align="center" label="用户账号" prop="userAccount"/>
      <el-table-column align="center" label="获得活期利息" prop="sourceType21Balance0" />
      <el-table-column align="center" label="获得固定利息" prop="sourceType21Balance1" />
      <el-table-column align="center" label="推荐奖" prop="sourceType23Balance" />
      <el-table-column align="center" label="团队奖" prop="sourceType24Balance" />
      <el-table-column align="center" label="平级奖" prop="sourceType25Balance" />
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:userIncomeSummary:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userIncomeSummary:remove']"
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

    <!-- 添加或修改用户收益信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="获得活期利息" prop="sourceType21Balance0">
          <el-input v-model="form.sourceType21Balance0" placeholder="请输入获得活期利息" />
        </el-form-item>
        <el-form-item label="获得固定利息" prop="sourceType21Balance1">
          <el-input v-model="form.sourceType21Balance1" placeholder="请输入获得固定利息" />
        </el-form-item>
        <el-form-item label="推荐奖" prop="sourceType23Balance">
          <el-input v-model="form.sourceType23Balance" placeholder="请输入推荐奖" />
        </el-form-item>
        <el-form-item label="团队奖" prop="sourceType24Balance">
          <el-input v-model="form.sourceType24Balance" placeholder="请输入团队奖" />
        </el-form-item>
        <el-form-item label="平级奖" prop="sourceType25Balance">
          <el-input v-model="form.sourceType25Balance" placeholder="请输入平级奖" />
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
import { listUserIncomeSummary, getUserIncomeSummary, delUserIncomeSummary, addUserIncomeSummary, updateUserIncomeSummary } from "@/api/xms/userIncomeSummary";

export default {
  name: "UserIncomeSummary",
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
      // 用户收益信息表格数据
      userIncomeSummaryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        userAccount: null,
        sourceType21Balance0: null,
        sourceType21Balance1: null,
        sourceType23Balance: null,
        sourceType24Balance: null,
        sourceType25Balance: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户收益信息列表 */
    getList() {
      this.loading = true;
      listUserIncomeSummary(this.queryParams).then(response => {
        this.userIncomeSummaryList = response.rows;
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
        userId: null,
        sourceType21Balance0: null,
        sourceType21Balance1: null,
        sourceType23Balance: null,
        sourceType24Balance: null,
        sourceType25Balance: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户收益信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids
      getUserIncomeSummary(userId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户收益信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != null) {
            updateUserIncomeSummary(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserIncomeSummary(this.form).then(response => {
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
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户收益信息编号为"' + userIds + '"的数据项？').then(function() {
        return delUserIncomeSummary(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userIncomeSummary/export', {
        ...this.queryParams
      }, `userIncomeSummary_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
