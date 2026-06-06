<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="阶段号 1-5" prop="stageNo">
        <el-input
          v-model="queryParams.stageNo"
          placeholder="请输入阶段号 1-5"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="达到该价格后可解锁" prop="unlockPrice">
        <el-input
          v-model="queryParams.unlockPrice"
          placeholder="请输入达到该价格后可解锁"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阶段名称" prop="stageName">
        <el-input
          v-model="queryParams.stageName"
          placeholder="请输入阶段名称"
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
          v-hasPermi="['xms:wealthVaultStageConfig:add']"
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
          v-hasPermi="['xms:wealthVaultStageConfig:edit']"
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
          v-hasPermi="['xms:wealthVaultStageConfig:remove']"
        >删除</el-button>
      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:wealthVaultStageConfig:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wealthVaultStageConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="false"/>
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="阶段号" align="center" prop="stageNo" />
      <el-table-column label="达到该价格后可解锁" align="center" prop="unlockPrice" />
      <el-table-column label="阶段名称" align="center" prop="stageName" />
      <el-table-column label="备注" align="center" prop="remark" />

      <el-table-column align="center" label="创建时间" prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:wealthVaultStageConfig:edit']"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:wealthVaultStageConfig:remove']"
          >删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改财富仓阶段解锁配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="阶段号 1-5" prop="stageNo">
          <el-input v-model="form.stageNo"
                    :disabled="true"
                    placeholder="请输入阶段号 1-5" />
        </el-form-item>

        <el-form-item label="达到该价格后可解锁" prop="unlockPrice">
          <el-input v-model="form.unlockPrice"
                    @input="sanitizeDecimalInput('unlockPrice')"
                    placeholder="请输入达到该价格后可解锁" />
        </el-form-item>

        <el-form-item label="阶段名称" prop="stageName">
          <el-input v-model="form.stageName"
                    maxlength="20" show-word-limit
                    placeholder="请输入阶段名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark"
                    maxlength="20" show-word-limit
                    placeholder="请输入备注" />
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
import { listWealthVaultStageConfig, getWealthVaultStageConfig, delWealthVaultStageConfig, addWealthVaultStageConfig, updateWealthVaultStageConfig } from "@/api/xms/wealthVaultStageConfig";

export default {
  name: "WealthVaultStageConfig",
  data() {
    const validateDecimalNumber = (rule, value, callback) => {
      if (value === null || value === undefined || value === "") {
        callback(new Error(rule.message));
        return;
      }
      if (!/^\d+(\.\d{1,2})?$/.test(value)) {
        callback(new Error("只能输入数字，且最多2位小数"));
        return;
      }
      callback();
    };
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
      // 财富仓阶段解锁配置表格数据
      wealthVaultStageConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stageNo: null,
        unlockPrice: null,
        stageName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        stageNo: [
          { required: true, message: "阶段号 1-5不能为空", trigger: "blur" }
        ],
        unlockPrice: [
          { required: true, message: "达到该价格后可解锁不能为空", trigger: "blur" },
          { validator: validateDecimalNumber, message: "达到该价格后可解锁不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    sanitizeDecimalInput(field) {
      let value = this.form[field];
      if (value === null || value === undefined) {
        return;
      }
      value = String(value)
        .replace(/[^\d.]/g, "")
        .replace(/^\./g, "")
        .replace(/\.{2,}/g, ".")
        .replace(".", "#DOT#")
        .replace(/\./g, "")
        .replace("#DOT#", ".")
        .replace(/^(\d+)\.(\d{0,2}).*$/, "$1.$2");
      this.$set(this.form, field, value);
    },
    /** 查询财富仓阶段解锁配置列表 */
    getList() {
      this.loading = true;
      listWealthVaultStageConfig(this.queryParams).then(response => {
        this.wealthVaultStageConfigList = response.rows;
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
        stageNo: null,
        unlockPrice: null,
        stageName: null,
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
      this.title = "添加财富仓阶段解锁配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWealthVaultStageConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改财富仓阶段解锁配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWealthVaultStageConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWealthVaultStageConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除财富仓阶段解锁配置编号为"' + ids + '"的数据项？').then(function() {
        return delWealthVaultStageConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/wealthVaultStageConfig/export', {
        ...this.queryParams
      }, `wealthVaultStageConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
