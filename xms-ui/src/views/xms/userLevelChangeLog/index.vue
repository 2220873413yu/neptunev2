<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="变动类型" label-width="120px" prop="changeType">
        <el-select v-model="queryParams.changeType" clearable placeholder="请选择变动类型">
          <el-option
            v-for="dict in dict.type.t_user_level_change_log_change_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="相关订单号" label-width="120px"prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入相关订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="总奖励金额" prop="totalReward">
        <el-input
          v-model="queryParams.totalReward"
          placeholder="请输入总奖励金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变更前历史最高等级" prop="historyMaxLevel">
        <el-input
          v-model="queryParams.historyMaxLevel"
          placeholder="请输入变更前历史最高等级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <el-form-item label="创建时间" label-width="120px">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
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
          v-hasPermi="['xms:userLevelChangeLog:add']"
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
          v-hasPermi="['xms:userLevelChangeLog:edit']"
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
          v-hasPermi="['xms:userLevelChangeLog:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:userLevelChangeLog:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userLevelChangeLogList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="原等级" prop="oldLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.oldLevel"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="新等级" prop="newLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.newLevel"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="变动类型" prop="changeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_level_change_log_change_type" :value="scope.row.changeType"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="相关订单号" prop="orderNo" />

      <el-table-column align="center" label="总奖励金额" prop="totalReward" />

      <el-table-column align="center" label="变更前历史最高等级" prop="historyMaxLevel">
      <template slot-scope="scope">
        <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.historyMaxLevel"/>
      </template>
      </el-table-column>

      <el-table-column align="center" label="是否发放奖励" prop="hasReward">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.hasReward"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="奖励详情(JSON格式)" prop="rewardDetail" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
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
            v-hasPermi="['xms:userLevelChangeLog:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userLevelChangeLog:remove']"
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

    <!-- 添加或修改用户等级变动日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="原等级" prop="oldLevel">
          <el-input v-model="form.oldLevel" placeholder="请输入原等级" />
        </el-form-item>
        <el-form-item label="新等级" prop="newLevel">
          <el-input v-model="form.newLevel" placeholder="请输入新等级" />
        </el-form-item>
        <el-form-item label="变动类型(1:升级,2:降级)" prop="changeType">
          <el-select v-model="form.changeType" placeholder="请选择变动类型(1:升级,2:降级)">
            <el-option
              v-for="dict in dict.type.t_user_level_change_log_change_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="相关订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入相关订单号" />
        </el-form-item>
        <el-form-item label="奖励详情(JSON格式)" prop="rewardDetail">
          <el-input v-model="form.rewardDetail" placeholder="请输入内容" type="textarea" />
        </el-form-item>
        <el-form-item label="总奖励金额" prop="totalReward">
          <el-input v-model="form.totalReward" placeholder="请输入总奖励金额" />
        </el-form-item>
        <el-form-item label="变更前历史最高等级" prop="historyMaxLevel">
          <el-input v-model="form.historyMaxLevel" placeholder="请输入变更前历史最高等级" />
        </el-form-item>
        <el-form-item label="是否发放奖励(0:否,1:是)" prop="hasReward">
          <el-input v-model="form.hasReward" placeholder="请输入是否发放奖励(0:否,1:是)" />
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
import { listUserLevelChangeLog, getUserLevelChangeLog, delUserLevelChangeLog, addUserLevelChangeLog, updateUserLevelChangeLog } from "@/api/xms/userLevelChangeLog";

export default {
  name: "UserLevelChangeLog",
  dicts: ['t_user_level_change_log_change_type','t_user_info_game_level','t_user_info_is_valid'],
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
      // 用户等级变动日志表格数据
      userLevelChangeLogList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否发放奖励(0:否,1:是)时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        oldLevel: null,
        newLevel: null,
        changeType: null,
        orderNo: null,
        rewardDetail: null,
        totalReward: null,
        historyMaxLevel: null,
        hasReward: null,
        createTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        oldLevel: [
          { required: true, message: "原等级不能为空", trigger: "blur" }
        ],
        newLevel: [
          { required: true, message: "新等级不能为空", trigger: "blur" }
        ],
        changeType: [
          { required: true, message: "变动类型(1:升级,2:降级)不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户等级变动日志列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listUserLevelChangeLog(this.queryParams).then(response => {
        this.userLevelChangeLogList = response.rows;
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
        oldLevel: null,
        newLevel: null,
        changeType: null,
        orderNo: null,
        rewardDetail: null,
        totalReward: null,
        historyMaxLevel: null,
        hasReward: null,
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
      this.title = "添加用户等级变动日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserLevelChangeLog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户等级变动日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserLevelChangeLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserLevelChangeLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户等级变动日志编号为"' + ids + '"的数据项？').then(function() {
        return delUserLevelChangeLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userLevelChangeLog/export', {
        ...this.queryParams
      }, `userLevelChangeLog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
