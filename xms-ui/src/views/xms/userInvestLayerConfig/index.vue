<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="层级编码" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择层级编码" clearable>
          <el-option
            v-for="dict in dict.type.t_user_invest_layer_config_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="最低投资额度" prop="minInvest">
        <el-input
          v-model="queryParams.minInvest"
          placeholder="请输入最低投资额度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="可获得层数" prop="layerCount">
        <el-input
          v-model="queryParams.layerCount"
          placeholder="请输入可获得层数"
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
          v-hasPermi="['xms:userInvestLayerConfig:add']"
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
          v-hasPermi="['xms:userInvestLayerConfig:edit']"
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
          v-hasPermi="['xms:userInvestLayerConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userInvestLayerConfig:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userInvestLayerConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" v-if="false"/>
      <el-table-column label="层级编码" align="center" prop="level">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_invest_layer_config_level" :value="scope.row.level"/>
        </template>
      </el-table-column>
      <el-table-column label="最低投资额度" align="center" prop="minInvest" />
      <el-table-column label="层级奖比例" align="center" prop="rewardRatio" >
        <template slot-scope="scope">
          {{scope.row.rewardRatio}}%
        </template>
      </el-table-column>
      <el-table-column label="可获得层数" align="center" prop="layerCount" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.level>0"
            v-hasPermi="['xms:userInvestLayerConfig:edit']"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userInvestLayerConfig:remove']"
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

    <!-- 添加或修改层奖配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="层级编码" prop="level">
          <el-select v-model="form.level" placeholder="请选择层级编码">
            <el-option
              v-for="dict in dict.type.t_user_invest_layer_config_level"
              :key="dict.value"
              :disabled="true"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="最低投资额度" prop="minInvest">
          <el-input v-model="form.minInvest"
                    placeholder="请输入最低投资额度"
                    type="number"
                    inputmode="decimal"
                    @input="form.minInvest = formatDecimal2(form.minInvest)" />
        </el-form-item>

        <el-form-item label="层级奖比例" prop="rewardRatio">
          <el-input v-model="form.rewardRatio"
                    @input="form.rewardRatio = formatDecimal2(form.rewardRatio)"
                    placeholder="请输入级差奖比例" />
          <div class="form-tip">层级奖比例以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>

        <el-form-item label="可获得层数" prop="layerCount">
          <el-input v-model="form.layerCount"
                    :disabled="true"
                    placeholder="请输入可获得层数" />
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
import { listUserInvestLayerConfig, getUserInvestLayerConfig, delUserInvestLayerConfig, addUserInvestLayerConfig, updateUserInvestLayerConfig } from "@/api/xms/userInvestLayerConfig";

export default {
  name: "UserInvestLayerConfig",
  dicts: ['t_user_invest_layer_config_level'],
  data() {
    const validateDecimalRate = (rule, value, callback) => {
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
      // 层奖配置表格数据
      userInvestLayerConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        level: null,
        minInvest: null,
        layerCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        level: [
          { required: true, message: "层级编码(如M1~M9)不能为空", trigger: "change" }
        ],
        minInvest: [
          { required: true, message: "最低投资额度不能为空", trigger: "blur" }
        ],rewardRatio: [
          { required: true, message: "层级奖比例不能为空", trigger: "blur" },
          { validator: validateDecimalRate, message: "层级奖比例不能为空", trigger: "blur" }
        ],
        layerCount: [
          { required: true, message: "可获得层数不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    formatDecimal2(value) {
      if (value === null || value === undefined) {
        return "";
      }
      const str = String(value).replace(/[^\d.]/g, "");
      const parts = str.split(".");
      const intPart = parts[0] || "";
      const decPart = parts[1] ? parts[1].slice(0, 2) : "";
      return decPart ? `${intPart}.${decPart}` : intPart;
    },
    /** 查询层奖配置列表 */
    getList() {
      this.loading = true;
      listUserInvestLayerConfig(this.queryParams).then(response => {
        this.userInvestLayerConfigList = response.rows;
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
        level: null,
        minInvest: null,
        layerCount: null,
        rewardRatio: null,
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
      this.title = "添加层奖配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserInvestLayerConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改层奖配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserInvestLayerConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserInvestLayerConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除层奖配置编号为"' + ids + '"的数据项？').then(function() {
        return delUserInvestLayerConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userInvestLayerConfig/export', {
        ...this.queryParams
      }, `userInvestLayerConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
