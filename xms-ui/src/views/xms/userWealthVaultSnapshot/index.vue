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
      <el-form-item label="1段" prop="seg1Amount">
        <el-input
          v-model="queryParams.seg1Amount"
          placeholder="请输入1段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="2段" prop="seg2Amount">
        <el-input
          v-model="queryParams.seg2Amount"
          placeholder="请输入2段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="3段" prop="seg3Amount">
        <el-input
          v-model="queryParams.seg3Amount"
          placeholder="请输入3段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="4段" prop="seg4Amount">
        <el-input
          v-model="queryParams.seg4Amount"
          placeholder="请输入4段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="5段" prop="seg5Amount">
        <el-input
          v-model="queryParams.seg5Amount"
          placeholder="请输入5段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="6段" prop="seg6Amount">
        <el-input
          v-model="queryParams.seg6Amount"
          placeholder="请输入6段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="7段" prop="seg7Amount">
        <el-input
          v-model="queryParams.seg7Amount"
          placeholder="请输入7段"
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
          v-hasPermi="['xms:userWealthVaultSnapshot:add']"
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
          v-hasPermi="['xms:userWealthVaultSnapshot:edit']"
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
          v-hasPermi="['xms:userWealthVaultSnapshot:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userWealthVaultSnapshot:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userWealthVaultSnapshotList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="序号" align="center" prop="snapshotId" />
      <el-table-column label="轮次ID" align="center" prop="stakeRoundId" />
      <el-table-column label="用户ID" align="center" prop="id" />
      <el-table-column label="1段" align="center" prop="seg1Amount" />
      <el-table-column label="2段" align="center" prop="seg2Amount" />
      <el-table-column label="3段" align="center" prop="seg3Amount" />
      <el-table-column label="4段" align="center" prop="seg4Amount" />
      <el-table-column label="5段" align="center" prop="seg5Amount" />
<!--      <el-table-column label="6段" align="center" prop="seg6Amount" />
      <el-table-column label="7段" align="center" prop="seg7Amount" />-->
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
            v-hasPermi="['xms:userWealthVaultSnapshot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userWealthVaultSnapshot:remove']"
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

    <!-- 添加或修改用户财富仓快照对话框 -->
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
        <el-form-item label="用户id" prop="id">
          <el-input v-model="form.id" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="1段" prop="seg1Amount">
          <el-input v-model="form.seg1Amount" placeholder="请输入1段" />
        </el-form-item>
        <el-form-item label="2段" prop="seg2Amount">
          <el-input v-model="form.seg2Amount" placeholder="请输入2段" />
        </el-form-item>
        <el-form-item label="3段" prop="seg3Amount">
          <el-input v-model="form.seg3Amount" placeholder="请输入3段" />
        </el-form-item>
        <el-form-item label="4段" prop="seg4Amount">
          <el-input v-model="form.seg4Amount" placeholder="请输入4段" />
        </el-form-item>
        <el-form-item label="5段" prop="seg5Amount">
          <el-input v-model="form.seg5Amount" placeholder="请输入5段" />
        </el-form-item>
        <el-form-item label="6段" prop="seg6Amount">
          <el-input v-model="form.seg6Amount" placeholder="请输入6段" />
        </el-form-item>
        <el-form-item label="7段" prop="seg7Amount">
          <el-input v-model="form.seg7Amount" placeholder="请输入7段" />
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
import { listUserWealthVaultSnapshot, getUserWealthVaultSnapshot, delUserWealthVaultSnapshot, addUserWealthVaultSnapshot, updateUserWealthVaultSnapshot } from "@/api/xms/userWealthVaultSnapshot";

export default {
  name: "UserWealthVaultSnapshot",
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
      // 用户财富仓快照表格数据
      userWealthVaultSnapshotList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stakeRoundId: null,
        id: null,
        snapshotTime: null,
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
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        seg1Amount: [
          { required: true, message: "1段不能为空", trigger: "blur" }
        ],
        seg2Amount: [
          { required: true, message: "2段不能为空", trigger: "blur" }
        ],
        seg3Amount: [
          { required: true, message: "3段不能为空", trigger: "blur" }
        ],
        seg4Amount: [
          { required: true, message: "4段不能为空", trigger: "blur" }
        ],
        seg5Amount: [
          { required: true, message: "5段不能为空", trigger: "blur" }
        ],
        seg6Amount: [
          { required: true, message: "6段不能为空", trigger: "blur" }
        ],
        seg7Amount: [
          { required: true, message: "7段不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户财富仓快照列表 */
    getList() {
      this.loading = true;
      listUserWealthVaultSnapshot(this.queryParams).then(response => {
        this.userWealthVaultSnapshotList = response.rows;
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
        seg1Amount: null,
        seg2Amount: null,
        seg3Amount: null,
        seg4Amount: null,
        seg5Amount: null,
        seg6Amount: null,
        seg7Amount: null,
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
      this.title = "添加用户财富仓快照";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const snapshotId = row.snapshotId || this.ids
      getUserWealthVaultSnapshot(snapshotId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户财富仓快照";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.snapshotId != null) {
            updateUserWealthVaultSnapshot(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserWealthVaultSnapshot(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户财富仓快照编号为"' + snapshotIds + '"的数据项？').then(function() {
        return delUserWealthVaultSnapshot(snapshotIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userWealthVaultSnapshot/export', {
        ...this.queryParams
      }, `userWealthVaultSnapshot_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
