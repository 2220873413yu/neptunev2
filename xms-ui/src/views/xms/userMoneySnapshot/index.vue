<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="轮次ID" prop="stakeRoundId">
        <el-input
          v-model="queryParams.stakeRoundId"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入轮次ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="用户ID" prop="id">
        <el-input
          v-model="queryParams.id"
          clearable
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="快照时间" prop="snapshotTime">
        <el-date-picker clearable
          v-model="queryParams.snapshotTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择快照时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="节点收益" prop="validNum1">
        <el-input
          v-model="queryParams.validNum1"
          placeholder="请输入节点收益"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="静态" prop="validNum2">
        <el-input
          v-model="queryParams.validNum2"
          placeholder="请输入静态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="动态" prop="validNum3">
        <el-input
          v-model="queryParams.validNum3"
          placeholder="请输入动态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="财富" prop="validNum4">
        <el-input
          v-model="queryParams.validNum4"
          placeholder="请输入财富"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="H代币(/魔盒/手续费)" prop="validNum5">
        <el-input
          v-model="queryParams.validNum5"
          placeholder="请输入H代币(/魔盒/手续费)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作室收益" prop="validNum6">
        <el-input
          v-model="queryParams.validNum6"
          placeholder="请输入工作室收益"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="贡献分" prop="validNum7">
        <el-input
          v-model="queryParams.validNum7"
          placeholder="请输入贡献分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="今日可提现动态" prop="validNum8">
        <el-input
          v-model="queryParams.validNum8"
          placeholder="请输入今日可提现动态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可用余额数" prop="validNum9">
        <el-input
          v-model="queryParams.validNum9"
          placeholder="请输入可用余额数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每次更新的唯一序号，后续可用来修正数据," prop="gtId">
        <el-input
          v-model="queryParams.gtId"
          placeholder="请输入每次更新的唯一序号，后续可用来修正数据,"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源订单" prop="sourceCode">
        <el-input
          v-model="queryParams.sourceCode"
          placeholder="请输入来源订单"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="来源用户ID" prop="sourceId">
        <el-input
          v-model="queryParams.sourceId"
          placeholder="请输入来源用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
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
          v-hasPermi="['xms:userMoneySnapshot:add']"
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
          v-hasPermi="['xms:userMoneySnapshot:edit']"
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
          v-hasPermi="['xms:userMoneySnapshot:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userMoneySnapshot:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userMoneySnapshotList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="snapshotId" />
      <el-table-column label="用户ID" align="center" prop="id" />
      <el-table-column label="轮次ID" align="center" prop="stakeRoundId" />
      <el-table-column label="节点收益" align="center" prop="validNum1" />
      <el-table-column label="静态" align="center" prop="validNum2" />
      <el-table-column label="动态" align="center" prop="validNum3" />
      <el-table-column label="财富" align="center" prop="validNum4" />
      <el-table-column label="魔盒" align="center" prop="validNum5" />
      <el-table-column label="工作室收益" align="center" prop="validNum6" />
      <el-table-column label="贡献分" align="center" prop="validNum7" />
      <el-table-column label="今日可提现动态" align="center" prop="validNum8" />
      <el-table-column label="快照时间" align="center" prop="snapshotTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.snapshotTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:userMoneySnapshot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userMoneySnapshot:remove']"
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

    <!-- 添加或修改用户余额快照对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="轮次id" prop="stakeRoundId">
          <el-input v-model="form.stakeRoundId" placeholder="请输入轮次id" />
        </el-form-item>
        <el-form-item label="快照时间" prop="snapshotTime">
          <el-date-picker clearable
            v-model="form.snapshotTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择快照时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="主键id" prop="id">
          <el-input v-model="form.id" placeholder="请输入主键id" />
        </el-form-item>
        <el-form-item label="节点收益" prop="validNum1">
          <el-input v-model="form.validNum1" placeholder="请输入节点收益" />
        </el-form-item>
        <el-form-item label="静态" prop="validNum2">
          <el-input v-model="form.validNum2" placeholder="请输入静态" />
        </el-form-item>
        <el-form-item label="动态" prop="validNum3">
          <el-input v-model="form.validNum3" placeholder="请输入动态" />
        </el-form-item>
        <el-form-item label="财富" prop="validNum4">
          <el-input v-model="form.validNum4" placeholder="请输入财富" />
        </el-form-item>
        <el-form-item label="H代币(/魔盒/手续费)" prop="validNum5">
          <el-input v-model="form.validNum5" placeholder="请输入H代币(/魔盒/手续费)" />
        </el-form-item>
        <el-form-item label="工作室收益" prop="validNum6">
          <el-input v-model="form.validNum6" placeholder="请输入工作室收益" />
        </el-form-item>
        <el-form-item label="贡献分" prop="validNum7">
          <el-input v-model="form.validNum7" placeholder="请输入贡献分" />
        </el-form-item>
        <el-form-item label="今日可提现动态" prop="validNum8">
          <el-input v-model="form.validNum8" placeholder="请输入今日可提现动态" />
        </el-form-item>
        <el-form-item label="可用余额数" prop="validNum9">
          <el-input v-model="form.validNum9" placeholder="请输入可用余额数" />
        </el-form-item>
        <el-form-item label="每次更新的唯一序号，后续可用来修正数据," prop="gtId">
          <el-input v-model="form.gtId" placeholder="请输入每次更新的唯一序号，后续可用来修正数据," />
        </el-form-item>
        <el-form-item label="来源订单" prop="sourceCode">
          <el-input v-model="form.sourceCode" placeholder="请输入来源订单" />
        </el-form-item>
        <el-form-item label="来源用户ID" prop="sourceId">
          <el-input v-model="form.sourceId" placeholder="请输入来源用户ID" />
        </el-form-item>
        <el-form-item label="删除标记,默认0,1:已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标记,默认0,1:已删除" />
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
import { listUserMoneySnapshot, getUserMoneySnapshot, delUserMoneySnapshot, addUserMoneySnapshot, updateUserMoneySnapshot } from "@/api/xms/userMoneySnapshot";

export default {
  name: "UserMoneySnapshot",
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
      // 用户余额快照表格数据
      userMoneySnapshotList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stakeRoundId: null,
        snapshotTime: null,
        id: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        stakeRoundId: [
          { required: true, message: "轮次id不能为空", trigger: "blur" }
        ],
        snapshotTime: [
          { required: true, message: "快照时间不能为空", trigger: "blur" }
        ],
        id: [
          { required: true, message: "主键id不能为空", trigger: "blur" }
        ],
        gtId: [
          { required: true, message: "每次更新的唯一序号，后续可用来修正数据,不能为空", trigger: "blur" }
        ],
        sourceCode: [
          { required: true, message: "来源订单不能为空", trigger: "blur" }
        ],
        sourceType: [
          { required: true, message: "来源类型(1.充值 2.提现 3.推荐奖 4.级差奖 5.平级奖 6.购买套餐 7.平台扣拨)不能为空", trigger: "change" }
        ],
        sourceId: [
          { required: true, message: "来源用户ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户余额快照列表 */
    getList() {
      this.loading = true;
      listUserMoneySnapshot(this.queryParams).then(response => {
        this.userMoneySnapshotList = response.rows;
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
        snapshotId: null,
        stakeRoundId: null,
        snapshotTime: null,
        id: null,
        validNum1: null,
        validNum2: null,
        validNum3: null,
        validNum4: null,
        validNum5: null,
        validNum6: null,
        validNum7: null,
        validNum8: null,
        validNum9: null,
        updateTime: null,
        gtId: null,
        sourceCode: null,
        sourceType: null,
        sourceId: null,
        deleted: null
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
      this.ids = selection.map(item => item.snapshotId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户余额快照";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const snapshotId = row.snapshotId || this.ids
      getUserMoneySnapshot(snapshotId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户余额快照";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.snapshotId != null) {
            updateUserMoneySnapshot(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserMoneySnapshot(this.form).then(response => {
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
      const snapshotIds = row.snapshotId || this.ids;
      this.$modal.confirm('是否确认删除用户余额快照编号为"' + snapshotIds + '"的数据项？').then(function() {
        return delUserMoneySnapshot(snapshotIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userMoneySnapshot/export', {
        ...this.queryParams
      }, `userMoneySnapshot_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
