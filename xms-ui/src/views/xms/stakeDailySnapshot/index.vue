<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="保险仓余额" prop="insuranceBalance">
        <el-input
          v-model="queryParams.insuranceBalance"
          placeholder="请输入保险仓余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本轮玩家累计参与总量(不含节点)" prop="playerStakeTotal">
        <el-input
          v-model="queryParams.playerStakeTotal"
          placeholder="请输入本轮玩家累计参与总量(不含节点)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本轮累计已发放工作室补贴" prop="studioSubsidyTotal">
        <el-input
          v-model="queryParams.studioSubsidyTotal"
          placeholder="请输入本轮累计已发放工作室补贴"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本轮累计已提取收益总额(静态+动态按100%口径)" prop="withdrawRewardTotalFull">
        <el-input
          v-model="queryParams.withdrawRewardTotalFull"
          placeholder="请输入本轮累计已提取收益总额(静态+动态按100%口径)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买积分的h余额" prop="buyPointTotal">
        <el-input
          v-model="queryParams.buyPointTotal"
          placeholder="请输入买积分的h余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="创建时间" label-width="120px">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="轮次编号" prop="stakeRoundId" label-width="120px">
        <el-input
          v-model="queryParams.stakeRoundId"
          placeholder="请输入轮次编号"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="用户待解锁财富仓余额" prop="lockedValidNum4">
        <el-input
          v-model="queryParams.lockedValidNum4"
          placeholder="请输入用户待解锁财富仓余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户财富仓余额" prop="totalValidNum4">
        <el-input
          v-model="queryParams.totalValidNum4"
          placeholder="请输入用户财富仓余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提现合约余额" prop="withdrawContractBalance">
        <el-input
          v-model="queryParams.withdrawContractBalance"
          placeholder="请输入提现合约余额"
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
          v-hasPermi="['xms:stakeDailySnapshot:add']"
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
          v-hasPermi="['xms:stakeDailySnapshot:edit']"
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
          v-hasPermi="['xms:stakeDailySnapshot:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:stakeDailySnapshot:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stakeDailySnapshotList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="false"/>
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>

      <el-table-column label="轮次编号" align="center" prop="stakeRoundId" />

      <el-table-column label="当天留存" align="center" >
        <template slot-scope="scope">
          {{ (Number(scope.row.playerStakeTotal || 0)
          + Number(scope.row.buyPointTotal || 0)
          - Number(scope.row.studioSubsidyTotal || 0)
          - Number(scope.row.withdrawRewardTotalFull || 0)
          + Number(scope.row.insuranceBalance || 0)
          + Number(scope.row.lockedValidNum4 || 0)
          + Number(scope.row.totalValidNum4 || 0)
          + Number(scope.row.withdrawContractBalance || 0)
        ).toFixed(6) }}
        </template>
      </el-table-column>
      <el-table-column label="保险仓余额" align="center" prop="insuranceBalance" />
      <el-table-column label="本轮实际可提现余额" align="center" >
        <template slot-scope="scope">
          {{ (Number(scope.row.playerStakeTotal || 0)
          + Number(scope.row.buyPointTotal || 0)
          - Number(scope.row.studioSubsidyTotal || 0)
          - Number(scope.row.withdrawRewardTotalFull || 0)).toFixed(6) }}
        </template>
      </el-table-column>
<!--      <el-table-column label="本轮玩家累计参与总量" align="center" prop="playerStakeTotal" />
      <el-table-column label="本轮累计已发放工作室补贴" align="center" prop="studioSubsidyTotal" />
      <el-table-column label="本轮累计已提取收益总额" align="center" prop="withdrawRewardTotalFull" />
      <el-table-column label="买积分的h余额" align="center" prop="buyPointTotal" />-->

      <el-table-column label="用户待解锁财富仓余额" align="center" prop="lockedValidNum4" />
      <el-table-column label="用户财富仓余额" align="center" prop="totalValidNum4" />
      <el-table-column label="提现合约余额" align="center" prop="withdrawContractBalance" />
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
            v-hasPermi="['xms:stakeDailySnapshot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:stakeDailySnapshot:remove']"
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

    <!-- 添加或修改每日质押数据快照对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="保险仓余额" prop="insuranceBalance">
          <el-input v-model="form.insuranceBalance" placeholder="请输入保险仓余额" />
        </el-form-item>
        <el-form-item label="本轮玩家累计参与总量(不含节点)" prop="playerStakeTotal">
          <el-input v-model="form.playerStakeTotal" placeholder="请输入本轮玩家累计参与总量(不含节点)" />
        </el-form-item>
        <el-form-item label="本轮累计已发放工作室补贴" prop="studioSubsidyTotal">
          <el-input v-model="form.studioSubsidyTotal" placeholder="请输入本轮累计已发放工作室补贴" />
        </el-form-item>
        <el-form-item label="本轮累计已提取收益总额(静态+动态按100%口径)" prop="withdrawRewardTotalFull">
          <el-input v-model="form.withdrawRewardTotalFull" placeholder="请输入本轮累计已提取收益总额(静态+动态按100%口径)" />
        </el-form-item>
        <el-form-item label="买积分的h余额" prop="buyPointTotal">
          <el-input v-model="form.buyPointTotal" placeholder="请输入买积分的h余额" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="轮次id" prop="stakeRoundId">
          <el-input v-model="form.stakeRoundId" placeholder="请输入轮次id" />
        </el-form-item>
        <el-form-item label="用户待解锁财富仓余额" prop="lockedValidNum4">
          <el-input v-model="form.lockedValidNum4" placeholder="请输入用户待解锁财富仓余额" />
        </el-form-item>
        <el-form-item label="用户财富仓余额" prop="totalValidNum4">
          <el-input v-model="form.totalValidNum4" placeholder="请输入用户财富仓余额" />
        </el-form-item>
        <el-form-item label="提现合约余额" prop="withdrawContractBalance">
          <el-input v-model="form.withdrawContractBalance" placeholder="请输入提现合约余额" />
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
import { listStakeDailySnapshot, getStakeDailySnapshot, delStakeDailySnapshot, addStakeDailySnapshot, updateStakeDailySnapshot } from "@/api/xms/stakeDailySnapshot";

export default {
  name: "StakeDailySnapshot",
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
      // 每日质押数据快照表格数据
      stakeDailySnapshotList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        insuranceBalance: null,
        playerStakeTotal: null,
        studioSubsidyTotal: null,
        withdrawRewardTotalFull: null,
        buyPointTotal: null,
        createTime: null,
        stakeRoundId: null,
        lockedValidNum4: null,
        totalValidNum4: null,
        withdrawContractBalance: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        playerStakeTotal: [
          { required: true, message: "本轮玩家累计参与总量(不含节点)不能为空", trigger: "blur" }
        ],
        studioSubsidyTotal: [
          { required: true, message: "本轮累计已发放工作室补贴不能为空", trigger: "blur" }
        ],
        withdrawRewardTotalFull: [
          { required: true, message: "本轮累计已提取收益总额(静态+动态按100%口径)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询每日质押数据快照列表 */
    getList() {
      this.loading = true;
      listStakeDailySnapshot(this.queryParams).then(response => {
        this.stakeDailySnapshotList = response.rows;
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
        insuranceBalance: null,
        playerStakeTotal: null,
        studioSubsidyTotal: null,
        withdrawRewardTotalFull: null,
        buyPointTotal: null,
        remark: null,
        createTime: null,
        updateTime: null,
        stakeRoundId: null,
        lockedValidNum4: null,
        totalValidNum4: null,
        withdrawContractBalance: null
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
      this.title = "添加每日质押数据快照";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStakeDailySnapshot(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改每日质押数据快照";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStakeDailySnapshot(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStakeDailySnapshot(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除每日质押数据快照编号为"' + ids + '"的数据项？').then(function() {
        return delStakeDailySnapshot(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/stakeDailySnapshot/export', {
        ...this.queryParams
      }, `stakeDailySnapshot_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
