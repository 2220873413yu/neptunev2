<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="等级" prop="level">
        <el-input
          v-model="queryParams.level"
          clearable
          placeholder="请输入等级"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="个人业绩" prop="performance">
        <el-input
          v-model="queryParams.performance"
          placeholder="请输入个人业绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小区业绩" prop="umbrellaPerformance">
        <el-input
          v-model="queryParams.umbrellaPerformance"
          placeholder="请输入小区业绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分成比例" prop="rewardRatio">
        <el-input
          v-model="queryParams.rewardRatio"
          placeholder="请输入分成比例"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否【小区算力 / 卡片等级】二选一: 0-否(只看小区算力),1-是(满足任一条件即可)" prop="isTwoChooseOne">
        <el-input
          v-model="queryParams.isTwoChooseOne"
          placeholder="请输入是否【小区算力 / 卡片等级】二选一: 0-否(只看小区算力),1-是(满足任一条件即可)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卡片类型 0:暂无" prop="cardType">
        <el-select v-model="queryParams.cardType" placeholder="请选择卡片类型 0:暂无" clearable>
          <el-option
            v-for="dict in dict.type.card_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>-->
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
          v-hasPermi="['xms:userLevelConfig:add']"
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
          v-hasPermi="['xms:userLevelConfig:edit']"
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
          v-hasPermi="['xms:userLevelConfig:remove']"
        >删除</el-button>
      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:userLevelConfig:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userLevelConfigList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="等级" prop="level">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.level"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="个人业绩" align="center" prop="performance" />-->
      <el-table-column align="center" label="小区业绩(质押量)" prop="umbrellaPerformance" />
      <el-table-column align="center" label="级差奖比例" prop="rewardRatio" >
        <template slot-scope="scope">
          {{scope.row.rewardRatio}}%
        </template>
      </el-table-column>

      <el-table-column align="center" label="平级奖比例" prop="peerRewardRatio" >
        <template slot-scope="scope">
          {{scope.row.peerRewardRatio}}%
        </template>
      </el-table-column>
      <el-table-column align="center" label="购买贡献分最低ACP代币限制" prop="minBuyAmount" >
        <template slot-scope="scope">
          {{scope.row.minBuyAmount}}ACP
        </template>
      </el-table-column>
<!--      <el-table-column label="是否二选一" align="center" prop="isTwoChooseOne">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.isTwoChooseOne"/>
        </template>
      </el-table-column>
      <el-table-column label="卡片类型 " align="center" prop="cardType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_type" :value="scope.row.cardType"/>
        </template>
      </el-table-column>-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
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
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.level > 0"
            v-hasPermi="['xms:userLevelConfig:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userLevelConfig:remove']"
          >删除</el-button>-->
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

    <!-- 添加或修改用户等级考核配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      append-to-body
      class="level-config-dialog"
      width="640px"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-form-item label="等级" prop="level">
          <el-select v-model="form.level" placeholder="请选择等级">
            <el-option
              v-for="dict in dict.type.t_user_info_game_level"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
              disabled
            ></el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="个人业绩" prop="performance">
          <el-input v-model="form.performance" placeholder="请输入个人业绩" />
        </el-form-item>-->
        <el-form-item label="小区业绩(质押量)" prop="umbrellaPerformance">
          <el-input v-model="form.umbrellaPerformance"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入小区业绩(质押量)" />
        </el-form-item>



<!--        <el-form-item label="是否二选一" prop="isTwoChooseOne">
          <el-select v-model="form.isTwoChooseOne" placeholder="请选择">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
          <div class="form-tip">否(只看小区算力),是(满足任一条件即可)</div>
        </el-form-item>
        -->
        <el-form-item label="是否工作室补贴" prop="hasStudioSubsidy" v-if="form.level > 0">
          <el-select v-model="form.hasStudioSubsidy" placeholder="请选择是否工作室补贴">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="购买贡献分最低h代币限制" prop="minBuyAmount">
          <el-input v-model="form.minBuyAmount"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入购买贡献分最低h代币限制" />
        </el-form-item>

        <el-form-item label="级差奖比例" prop="rewardRatio">
          <el-input v-model="form.rewardRatio"
                    @input="sanitizeDecimalInput('rewardRatio')"
                    placeholder="请输入级差奖比例" />
          <div class="form-tip">级差奖比例以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>

        <el-form-item label="平级奖比例" prop="peerRewardRatio" v-if="form.level>=4">
          <el-input v-model="form.peerRewardRatio"
                    @input="sanitizeDecimalInput('peerRewardRatio')"
                    placeholder="请输入平级奖比例" />
          <div class="form-tip">平级奖比例以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
<!--        <el-form-item label="备注" prop="remark">
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
import { listUserLevelConfig, getUserLevelConfig, delUserLevelConfig, addUserLevelConfig, updateUserLevelConfig } from "@/api/xms/userLevelConfig";

export default {
  name: "UserLevelConfig",
  dicts: ['card_type','t_user_info_game_level','t_user_info_is_valid'],
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
      // 用户等级考核配置表格数据
      userLevelConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        level: null,
        performance: null,
        umbrellaPerformance: null,
        rewardRatio: null,
        isTwoChooseOne: null,
        cardType: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        umbrellaPerformance: [
          { required: true, message: "小区业绩不能为空", trigger: "blur" }
        ],
        isTwoChooseOne: [
          { required: true, message: "是否二选一不能为空", trigger: "blur" }
        ],
        rewardRatio: [
          { required: true, message: "极差奖比例不能为空", trigger: "blur" },
          { validator: validateDecimalRate, message: "极差奖比例不能为空", trigger: "blur" }
        ],
        peerRewardRatio: [
          { required: true, message: "平级奖比例不能为空", trigger: "blur" },
          { validator: validateDecimalRate, message: "平级奖比例不能为空", trigger: "blur" }
        ],
        minBuyAmount: [
          { required: true, message: "购买贡献分最低h代币限制不能为空", trigger: "blur" }
        ],
        hasStudioSubsidy: [
          { required: true, message: "请选择是否工作室补贴不能为空", trigger: "blur" }
        ],
        cardType: [
          { required: true, message: "卡片类型不能为空", trigger: "blur" }
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
    /** 查询用户等级考核配置列表 */
    getList() {
      this.loading = true;
      listUserLevelConfig(this.queryParams).then(response => {
        this.userLevelConfigList = response.rows;
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
        performance: null,
        umbrellaPerformance: null,
        rewardRatio: null,
        isTwoChooseOne: null,
        minBuyAmount: null,
        cardType: null,
        createTime: null,
        updateTime: null,
        peerRewardRatio: null,
        hasStudioSubsidy: null,
        remark: null,
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
      this.title = "添加用户等级考核配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserLevelConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户等级考核配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserLevelConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserLevelConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户等级考核配置编号为"' + ids + '"的数据项？').then(function() {
        return delUserLevelConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userLevelConfig/export', {
        ...this.queryParams
      }, `userLevelConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style>
/* 等级配置弹窗：label 保持单行，输入区域限制最大宽度，避免过宽 */
.level-config-dialog .el-form-item__label {
  white-space: nowrap;
}
.level-config-dialog .el-form-item__content {
  max-width: 420px;
}

/* 表单说明文字 */
.form-tip {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}
</style>

