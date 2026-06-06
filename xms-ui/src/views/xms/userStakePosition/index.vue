<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId"  label-width="120px">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钱包地址" prop="userAccount" label-width="120px">
        <el-input
          v-model="queryParams.userAccount"
          placeholder="请输入钱包地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="当前总质押金额" prop="totalStakeAmount">
        <el-input
          v-model="queryParams.totalStakeAmount"
          placeholder="请输入当前总质押金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前日收益率(如1%=0.01)" prop="currentDayRate">
        <el-input
          v-model="queryParams.currentDayRate"
          placeholder="请输入当前日收益率(如1%=0.01)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="连续未提取收益天数" prop="continuousNoWithdrawDays">
        <el-input
          v-model="queryParams.continuousNoWithdrawDays"
          placeholder="请输入连续未提取收益天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="累计收益" prop="totalReward">
        <el-input
          v-model="queryParams.totalReward"
          placeholder="请输入累计收益"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当日收益(可选缓存)" prop="todayReward">
        <el-input
          v-model="queryParams.todayReward"
          placeholder="请输入当日收益(可选缓存)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最近结算日期" prop="lastSettleDate">
        <el-date-picker clearable
          v-model="queryParams.lastSettleDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择最近结算日期">
        </el-date-picker>
      </el-form-item>-->

      <el-form-item label="轮次ID" prop="stakeRoundId"  label-width="120px">
        <el-input
          v-model="queryParams.stakeRoundId"
          placeholder="请输入轮次表ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="保险仓赔付是否有资格" prop="insuranceQualifyStatus"  label-width="120px">
        <el-select v-model="queryParams.insuranceQualifyStatus" placeholder="请选择保险仓赔付是否有资格" clearable>
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="领取赔付是否有资格" prop="insuranceCompensationQualifyStatus"  label-width="120px">
        <el-select v-model="queryParams.insuranceCompensationQualifyStatus" placeholder="请选择领取赔付是否有资格" clearable>
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="创建时间"  label-width="120px">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
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
          v-hasPermi="['xms:userStakePosition:add']"
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
          v-hasPermi="['xms:userStakePosition:edit']"
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
          v-hasPermi="['xms:userStakePosition:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userStakePosition:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userStakePositionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="轮次ID" align="center" prop="stakeRoundId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="钱包地址" align="center" prop="userAccount" />
      <el-table-column label="总质押金额" align="center" prop="totalStakeAmount" />
      <el-table-column label="日收益率" align="center" prop="currentDayRate" >
      <template slot-scope="scope">
        {{scope.row.currentDayRate}}%
      </template>
      </el-table-column>
      <el-table-column label="连续未提取收益天数" align="center" prop="continuousNoWithdrawDays" />
      <el-table-column label="累计提现(静态)" align="center" prop="totalWithdrawalStatic" />
      <el-table-column label="累计提现(动态)" align="center" prop="totalWithdrawalDynamic" />
      <el-table-column label="累计提现(工作室补贴)" align="center" prop="totalWithdrawalStudioSubsidy" />
      <el-table-column label="累计收益(静态)" align="center" prop="totalReward" />
      <el-table-column label="累计收益(动态)" align="center" prop="dynamicReward" />
      <el-table-column label="当日收益" align="center" prop="todayReward" />
<!--      <el-table-column label="最近结算日期" align="center" prop="lastSettleDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastSettleDate) }}</span>
        </template>
      </el-table-column>-->
      <el-table-column align="status" label="订单状态">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_stake_position_status" :value="scope.row.status"/>
        </template>
      </el-table-column>

      <el-table-column label="保险仓赔付是否有资格" align="center" prop="insuranceQualifyStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.insuranceQualifyStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="个人亏损额" align="center" prop="personalLossAmount" />
      <el-table-column label="工作室补贴" align="center" prop="studioSubsidy" />
      <el-table-column label="可赔付上限" align="center" prop="allCompensationLimit" />
      <el-table-column label="剩余可赔付" align="center" prop="remainingCompensationLimit" />
      <el-table-column label="领取赔付是否有资格" align="center" prop="insuranceCompensationQualifyStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.insuranceCompensationQualifyStatus"/>
        </template>
      </el-table-column>
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
            v-hasPermi="['xms:userStakePosition:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userStakePosition:remove']"
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

    <!-- 添加或修改用户质押持仓汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="当前总质押金额" prop="totalStakeAmount">
          <el-input v-model="form.totalStakeAmount" placeholder="请输入当前总质押金额" />
        </el-form-item>
        <el-form-item label="当前日收益率(如1%=0.01)" prop="currentDayRate">
          <el-input v-model="form.currentDayRate" placeholder="请输入当前日收益率(如1%=0.01)" />
        </el-form-item>
        <el-form-item label="连续未提取收益天数" prop="continuousNoWithdrawDays">
          <el-input v-model="form.continuousNoWithdrawDays" placeholder="请输入连续未提取收益天数" />
        </el-form-item>
        <el-form-item label="累计收益" prop="totalReward">
          <el-input v-model="form.totalReward" placeholder="请输入累计收益" />
        </el-form-item>
        <el-form-item label="当日收益(可选缓存)" prop="todayReward">
          <el-input v-model="form.todayReward" placeholder="请输入当日收益(可选缓存)" />
        </el-form-item>
        <el-form-item label="最近结算日期" prop="lastSettleDate">
          <el-date-picker clearable
            v-model="form.lastSettleDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最近结算日期">
          </el-date-picker>
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
import { listUserStakePosition, getUserStakePosition, delUserStakePosition, addUserStakePosition, updateUserStakePosition } from "@/api/xms/userStakePosition";

export default {
  name: "UserStakePosition",
  dicts: ['t_user_stake_position_status','t_user_info_is_valid'],
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
      // 用户质押持仓汇总表格数据
      userStakePositionList: [],
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
        userId: null,
        totalStakeAmount: null,
        insuranceQualifyStatus: null,
        userAccount: null,
        insuranceCompensationQualifyStatus: null,
        currentDayRate: null,
        continuousNoWithdrawDays: null,
        totalReward: null,
        todayReward: null,
        lastSettleDate: null,
        status: null,
        createTime: null,
        stakeRoundId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        totalStakeAmount: [
          { required: true, message: "当前总质押金额不能为空", trigger: "blur" }
        ],
        currentDayRate: [
          { required: true, message: "当前日收益率(如1%=0.01)不能为空", trigger: "blur" }
        ],
        continuousNoWithdrawDays: [
          { required: true, message: "连续未提取收益天数不能为空", trigger: "blur" }
        ],
        totalReward: [
          { required: true, message: "累计收益不能为空", trigger: "blur" }
        ],
        todayReward: [
          { required: true, message: "当日收益(可选缓存)不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态:1正常,2:爆仓不能为空", trigger: "change" }
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
    /** 查询用户质押持仓汇总列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listUserStakePosition(this.queryParams).then(response => {
        this.userStakePositionList = response.rows;
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
        totalStakeAmount: null,
        currentDayRate: null,
        continuousNoWithdrawDays: null,
        totalReward: null,
        todayReward: null,
        lastSettleDate: null,
        status: null,
        createTime: null,
        updateTime: null,
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
      this.title = "添加用户质押持仓汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getUserStakePosition(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户质押持仓汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUserStakePosition(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserStakePosition(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户质押持仓汇总编号为"' + ids + '"的数据项？').then(function() {
        return delUserStakePosition(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userStakePosition/export', {
        ...this.queryParams
      }, `userStakePosition_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
