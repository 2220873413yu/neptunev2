<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="等级" prop="level">
        <el-select v-model="queryParams.level" clearable placeholder="请选择等级">
          <el-option
            v-for="dict in dict.type.t_user_info_game_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

<!--      <el-form-item label="团队业绩" prop="umbrellaPerformance">
        <el-input
          v-model="queryParams.umbrellaPerformance"
          placeholder="请输入团队业绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小区业绩" prop="communityPerformance">
        <el-input
          v-model="queryParams.communityPerformance"
          placeholder="请输入小区业绩"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="团队奖比例" prop="teamRewardRatio">
        <el-input
          v-model="queryParams.teamRewardRatio"
          placeholder="请输入团队奖比例"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="全网静态分红比例" prop="staticRewardRatio">
        <el-input
          v-model="queryParams.staticRewardRatio"
          placeholder="请输入全网静态分红比例"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['xms:w3UserLevelConfig:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:w3UserLevelConfig:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xms:w3UserLevelConfig:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:w3UserLevelConfig:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="w3UserLevelConfigList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="等级" prop="level">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.level"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="个人销毁" prop="performance" >
        <template slot-scope="scope">
          {{scope.row.performance}} U
        </template>
      </el-table-column>

      <el-table-column align="center" label="团队销毁金额" prop="umbrellaPerformance" >
        <template slot-scope="scope">
          {{scope.row.umbrellaPerformance}} U
        </template>
      </el-table-column>

      <el-table-column align="center" label="是否考核伞下级别" prop="isUmbrellaLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.isUmbrellaLevel"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="几条线" prop="umbrellaCount" >
      <template slot-scope="scope">
        {{scope.row.umbrellaCount}}
      </template>
      </el-table-column>

      <el-table-column align="center" label="伞下级别" prop="umbrellaLevel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.umbrellaLevel"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="团队收益(极差)" prop="rewardRatio" >
        <template slot-scope="scope">
          {{scope.row.rewardRatio}}%
        </template>
      </el-table-column>

      <el-table-column align="center" label="平级奖" prop="peerRewardRatio" >
        <template slot-scope="scope">
          {{scope.row.peerRewardRatio}}%
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.level!=0"
            v-hasPermi="['xms:w3UserLevelConfig:edit']"
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
            v-hasPermi="['xms:w3UserLevelConfig:remove']"
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
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
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
        <el-form-item v-if="form.level !=0" label="个人销毁" prop="performance">
          <el-input v-model="form.performance"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入" />
        </el-form-item>

        <el-form-item v-if="form.level !=0" label="团队销毁金额" prop="umbrellaPerformance">
          <el-input v-model="form.umbrellaPerformance"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入" />
        </el-form-item>

        <el-form-item label="是否考核伞下级别" prop="isUmbrellaLevel">
          <el-select v-model="form.isUmbrellaLevel" placeholder="请选择">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="form.isUmbrellaLevel == 1" label="几条线" prop="umbrellaCount">
          <el-input v-model="form.umbrellaCount"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入" />
        </el-form-item>

        <el-form-item v-if="form.isUmbrellaLevel == 1" label="伞下级别" prop="umbrellaLevel">
          <el-select v-model="form.umbrellaLevel" placeholder="请选择等级">
            <el-option
              v-for="dict in dict.type.t_user_info_game_level"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="收益百分比" prop="rewardRatio">
          <el-input v-model="form.rewardRatio"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>

        <el-form-item label="平级奖百分比" prop="peerRewardRatio">
          <el-input v-model="form.peerRewardRatio"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>

        <el-form-item  :rules="[{ required: true, message: '请输入google验证码' }]" label="google验证码" prop="autoCode">
          <el-input v-model="form.autoCode" placeholder="google验证码"></el-input>
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
import { listW3UserLevelConfig, getW3UserLevelConfig, delW3UserLevelConfig, addW3UserLevelConfig, updateW3UserLevelConfig } from "@/api/xms/w3UserLevelConfig";

export default {
  name: "W3UserLevelConfig",
  dicts: ['t_user_info_game_level', 't_w3_user_level_config_type','t_user_info_is_valid'],
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
      // 用户等级考核配置表格数据
      w3UserLevelConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        level: null,
        type: null,
        umbrellaPerformance: null,
        communityPerformance: null,
        teamRewardRatio: null,
        staticRewardRatio: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        level: [
          { required: true, message: "等级不能为空", trigger: "change" }
        ],
        performance: [
          { required: true, message: "个人销毁不能为空", trigger: "change" }
        ],
        umbrellaPerformance: [
          { required: true, message: "团队销毁金额不能为空", trigger: "change" }
        ],
        umbrellaLevel: [
          { required: true, message: "伞下级别不能为空", trigger: "change" }
        ],
        umbrellaCount: [
          { required: true, message: "几条线不能为空", trigger: "change" }
        ],
        rewardRatio: [
          { required: true, message: "静态收益百分比不能为空", trigger: "change" }
        ],
        peerRewardRatio: [
          { required: true, message: "平级奖百分比不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户等级考核配置列表 */
    getList() {
      this.loading = true;
      listW3UserLevelConfig(this.queryParams).then(response => {
        this.w3UserLevelConfigList = response.rows;
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
        umbrellaPerformance: null,
        performance: null,
        autoCode: null,
        isUmbrellaLevel: null,
        umbrellaCount: null,
        peerRewardRatio: null,
        umbrellaLevel: null,
        rewardRatio: null
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
      getW3UserLevelConfig(id).then(response => {
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
            updateW3UserLevelConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addW3UserLevelConfig(this.form).then(response => {
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
        return delW3UserLevelConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/w3UserLevelConfig/export', {
        ...this.queryParams
      }, `w3UserLevelConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style scoped>
.remark-ellipsis {
  display: inline-block;
  width: 100%;
  white-space: nowrap; /* 禁止换行 */
  overflow: hidden; /* 隐藏超出部分 */
  text-overflow: ellipsis; /* 显示省略号 */
}
.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
