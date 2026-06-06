<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="初始日收益率(如1=1%)" prop="initialDailyRate">
        <el-input
          v-model="queryParams.initialDailyRate"
          placeholder="请输入初始日收益率(如1=1%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最低日收益率(如5=5%)" prop="minDailyRate">
        <el-input
          v-model="queryParams.minDailyRate"
          placeholder="请输入最低日收益率(如5=5%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最高日收益率(如1.5=1.5%)" prop="maxDailyRate">
        <el-input
          v-model="queryParams.maxDailyRate"
          placeholder="请输入最高日收益率(如1.5=1.5%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="连续未提取收益达到N天触发增长" prop="growthConsecutiveDays">
        <el-input
          v-model="queryParams.growthConsecutiveDays"
          placeholder="请输入连续未提取收益达到N天触发增长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每次增长步长(如0.1=0.1%)" prop="growthRateStep">
        <el-input
          v-model="queryParams.growthRateStep"
          placeholder="请输入每次增长步长(如0.1=0.1%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单次提取超过当前参与量比例阈值(如10=10%)" prop="decayWithdrawThresholdRatio">
        <el-input
          v-model="queryParams.decayWithdrawThresholdRatio"
          placeholder="请输入单次提取超过当前参与量比例阈值(如10=10%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每次衰减步长(如0.1=0.1%)" prop="decayRateStep">
        <el-input
          v-model="queryParams.decayRateStep"
          placeholder="请输入每次衰减步长(如0.1=0.1%)"
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
          v-hasPermi="['xms:userYieldRateConfig:add']"
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
          v-hasPermi="['xms:userYieldRateConfig:edit']"
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
          v-hasPermi="['xms:userYieldRateConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userYieldRateConfig:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userYieldRateConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="初始日收益率" align="center" prop="initialDailyRate" >
        <template slot-scope="scope">
          {{scope.row.initialDailyRate}}%
        </template>
      </el-table-column>
      <el-table-column label="最低日收益率" align="center" prop="minDailyRate" >
        <template slot-scope="scope">
          {{scope.row.minDailyRate}}%
        </template>
      </el-table-column>
      <el-table-column label="最高日收益率" align="center" prop="maxDailyRate" >
        <template slot-scope="scope">
          {{scope.row.maxDailyRate}}%
        </template>
      </el-table-column>
      <el-table-column label="连续未提取收益达到N天触发增长" align="center" prop="growthConsecutiveDays" />
      <el-table-column label="每次增长步长" align="center" prop="growthRateStep">
      <template slot-scope="scope">
        {{scope.row.growthRateStep}}%
      </template>
      </el-table-column>
<!--      <el-table-column label="单次提取超过当前参与量比例阈值(如10=10%)" align="center" prop="decayWithdrawThresholdRatio" />-->
      <el-table-column label="每次衰减步长(如0.1=0.1%)" align="center" prop="decayRateStep">
      <template slot-scope="scope">
        {{scope.row.decayRateStep}}%
      </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:userYieldRateConfig:edit']"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userYieldRateConfig:remove']"
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

    <!-- 添加或修改用户收益率规则配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="初始日收益率" prop="initialDailyRate">
          <el-input v-model="form.initialDailyRate" placeholder="请输入初始日收益率(如1=1%)" @input="sanitizeRateInput('initialDailyRate')" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
        <el-form-item label="最低日收益率" prop="minDailyRate">
          <el-input v-model="form.minDailyRate" placeholder="请输入最低日收益率(如5=5%)" @input="sanitizeRateInput('minDailyRate')" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
        <el-form-item label="最高日收益率" prop="maxDailyRate">
          <el-input v-model="form.maxDailyRate" placeholder="请输入最高日收益率(如1.5=1.5%)" @input="sanitizeRateInput('maxDailyRate')" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
        <el-form-item label="连续未提取收益达到N天触发增长" prop="growthConsecutiveDays">
          <el-input v-model="form.growthConsecutiveDays"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入连续未提取收益达到N天触发增长" />
        </el-form-item>
<!--        <el-form-item label="每次增长步长(如0.1=0.1%)" prop="growthRateStep">
          <el-input v-model="form.growthRateStep" placeholder="请输入每次增长步长(如0.1=0.1%)" />
        </el-form-item>-->
<!--        <el-form-item label="单次提取超过当前参与量比例阈值(如10=10%)" prop="decayWithdrawThresholdRatio">
          <el-input v-model="form.decayWithdrawThresholdRatio" placeholder="请输入单次提取超过当前参与量比例阈值(如10=10%)" />
        </el-form-item>-->
<!--        <el-form-item label="每次衰减步长(如0.1=0.1%)" prop="decayRateStep">
          <el-input v-model="form.decayRateStep" placeholder="请输入每次衰减步长(如0.1=0.1%)" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="删除标记,默认0,1已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标记,默认0,1已删除" />
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUserYieldRateConfig, getUserYieldRateConfig, delUserYieldRateConfig, addUserYieldRateConfig, updateUserYieldRateConfig } from "@/api/xms/userYieldRateConfig";

export default {
  name: "UserYieldRateConfig",
  data() {
    const validateRate = (rule, value, callback) => {
      if (value === null || value === undefined || value === "") {
        callback(new Error(rule.message));
        return;
      }
      if (!/^\d+(\.\d{1,2})?$/.test(value)) {
        callback(new Error("只能输入纯数字，且最多两位小数"));
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
      // 用户收益率规则配置表格数据
      userYieldRateConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        initialDailyRate: null,
        minDailyRate: null,
        maxDailyRate: null,
        growthConsecutiveDays: null,
        growthRateStep: null,
        decayWithdrawThresholdRatio: null,
        decayRateStep: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        initialDailyRate: [
          { required: true, message: "初始日收益率(如1=1%)不能为空", trigger: "blur" },
          { validator: validateRate, message: "初始日收益率(如1=1%)不能为空", trigger: "blur" }
        ],
        minDailyRate: [
          { required: true, message: "最低日收益率(如5=5%)不能为空", trigger: "blur" },
          { validator: validateRate, message: "最低日收益率(如5=5%)不能为空", trigger: "blur" }
        ],
        maxDailyRate: [
          { required: true, message: "最高日收益率(如1.5=1.5%)不能为空", trigger: "blur" },
          { validator: validateRate, message: "最高日收益率(如1.5=1.5%)不能为空", trigger: "blur" }
        ],
        growthConsecutiveDays: [
          { required: true, message: "连续未提取收益达到N天触发增长不能为空", trigger: "blur" }
        ],
        growthRateStep: [
          { required: true, message: "每次增长步长(如0.1=0.1%)不能为空", trigger: "blur" }
        ],
        decayWithdrawThresholdRatio: [
          { required: true, message: "单次提取超过当前参与量比例阈值(如10=10%)不能为空", trigger: "blur" }
        ],
        decayRateStep: [
          { required: true, message: "每次衰减步长(如0.1=0.1%)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户收益率规则配置列表 */
    getList() {
      this.loading = true;
      listUserYieldRateConfig(this.queryParams).then(response => {
        this.userYieldRateConfigList = response.rows;
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
        initialDailyRate: null,
        minDailyRate: null,
        maxDailyRate: null,
        growthConsecutiveDays: null,
        growthRateStep: null,
        decayWithdrawThresholdRatio: null,
        decayRateStep: null,
        remark: null,
        createTime: null,
        updateTime: null,
        deleted: null,
        createBy: null,
        updateBy: null
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
      this.title = "添加用户收益率规则配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserYieldRateConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户收益率规则配置";
      });
    },
    sanitizeRateInput(field) {
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
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserYieldRateConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserYieldRateConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户收益率规则配置编号为"' + ids + '"的数据项？').then(function() {
        return delUserYieldRateConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userYieldRateConfig/export', {
        ...this.queryParams
      }, `userYieldRateConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
