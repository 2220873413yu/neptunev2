<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item label="轮次开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择轮次开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="爆仓触发时间" prop="liquidationTime">
        <el-date-picker clearable
          v-model="queryParams.liquidationTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择爆仓触发时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="轮次结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择轮次结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="本轮玩家累计参与总量(不含节点)" prop="playerStakeTotal">
        <el-input
          v-model="queryParams.playerStakeTotal"
          placeholder="请输入本轮玩家累计参与总量(不含节点)"
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
      <el-form-item label="爆仓时左值快照(玩家累计参与总量)" prop="liquidationLeftValue">
        <el-input
          v-model="queryParams.liquidationLeftValue"
          placeholder="请输入爆仓时左值快照(玩家累计参与总量)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="爆仓时右值快照(补贴+提取收益)" prop="liquidationRightValue">
        <el-input
          v-model="queryParams.liquidationRightValue"
          placeholder="请输入爆仓时右值快照(补贴+提取收益)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>-->
      <el-form-item label="轮次编号" prop="id">
        <el-input
          v-model="queryParams.id"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入轮次编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['xms:stakeRound:add']"
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
          v-hasPermi="['xms:stakeRound:edit']"
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
          v-hasPermi="['xms:stakeRound:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:stakeRound:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stakeRoundList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="轮次编号" align="center" prop="id" />
      <el-table-column label="质押轮次状态" align="center" prop="status" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_stake_round_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="保险仓余额" align="center" prop="insuranceBalance" />
      <el-table-column label="轮次开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本轮实际可提现余额" align="center" >
        <template slot-scope="scope">
          {{ (Number(scope.row.playerStakeTotal || 0)
            + Number(scope.row.buyPointTotal || 0)
            - Number(scope.row.studioSubsidyTotal || 0)
            - Number(scope.row.withdrawRewardTotalFull || 0)).toFixed(6) }}
        </template>
      </el-table-column>
      <el-table-column label="本轮玩家累计参与总量" align="center" prop="playerStakeTotal" />
      <el-table-column label="买贡献分花费的h代币数量" align="center" prop="buyPointTotal" />
      <el-table-column label="本轮累计已提现工作室补贴" align="center" prop="studioSubsidyTotal" />
      <el-table-column label="本轮累计已提取收益总额" align="center" prop="withdrawRewardTotalFull" />
<!--      <el-table-column label="爆仓时左值快照(玩家累计参与总量)" align="center" prop="liquidationLeftValue" />
      <el-table-column label="爆仓时右值快照(补贴+提取收益)" align="center" prop="liquidationRightValue" />-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="爆仓触发时间" align="center" prop="liquidationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.liquidationTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="轮次结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:stakeRound:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:stakeRound:remove']"
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

    <!-- 添加或修改全局质押轮次对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="轮次开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择轮次开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="爆仓触发时间" prop="liquidationTime">
          <el-date-picker clearable
            v-model="form.liquidationTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择爆仓触发时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="轮次结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择轮次结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="本轮玩家累计参与总量(不含节点)" prop="playerStakeTotal">
          <el-input v-model="form.playerStakeTotal" placeholder="请输入本轮玩家累计参与总量(不含节点)" />
        </el-form-item>
        <el-form-item label="本轮累计已提现工作室补贴" prop="studioSubsidyTotal">
          <el-input v-model="form.studioSubsidyTotal" placeholder="请输入本轮累计已提现工作室补贴" />
        </el-form-item>
        <el-form-item label="本轮累计已提取收益总额(静态+动态按100%口径)" prop="withdrawRewardTotalFull">
          <el-input v-model="form.withdrawRewardTotalFull" placeholder="请输入本轮累计已提取收益总额(静态+动态按100%口径)" />
        </el-form-item>
        <el-form-item label="爆仓时左值快照(玩家累计参与总量)" prop="liquidationLeftValue">
          <el-input v-model="form.liquidationLeftValue" placeholder="请输入爆仓时左值快照(玩家累计参与总量)" />
        </el-form-item>
        <el-form-item label="爆仓时右值快照(补贴+提取收益)" prop="liquidationRightValue">
          <el-input v-model="form.liquidationRightValue" placeholder="请输入爆仓时右值快照(补贴+提取收益)" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listStakeRound, getStakeRound, delStakeRound, addStakeRound, updateStakeRound } from "@/api/xms/stakeRound";

export default {
  name: "StakeRound",
  dicts: ['t_stake_round_status'],
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
      // 全局质押轮次表格数据
      stakeRoundList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
        startTime: null,
        id: null,
        liquidationTime: null,
        endTime: null,
        playerStakeTotal: null,
        studioSubsidyTotal: null,
        withdrawRewardTotalFull: null,
        liquidationLeftValue: null,
        liquidationRightValue: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "状态:0进行中,1已爆仓,2已结算,3已关闭不能为空", trigger: "change" }
        ],
        startTime: [
          { required: true, message: "轮次开始时间不能为空", trigger: "blur" }
        ],
        playerStakeTotal: [
          { required: true, message: "本轮玩家累计参与总量(不含节点)不能为空", trigger: "blur" }
        ],
        studioSubsidyTotal: [
          { required: true, message: "本轮累计已提现工作室补贴不能为空", trigger: "blur" }
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
    /** 查询全局质押轮次列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listStakeRound(this.queryParams).then(response => {
        this.stakeRoundList = response.rows;
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
        status: null,
        startTime: null,
        liquidationTime: null,
        endTime: null,
        playerStakeTotal: null,
        studioSubsidyTotal: null,
        withdrawRewardTotalFull: null,
        liquidationLeftValue: null,
        liquidationRightValue: null,
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
      this.title = "添加全局质押轮次";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStakeRound(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改全局质押轮次";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStakeRound(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStakeRound(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除全局质押轮次编号为"' + ids + '"的数据项？').then(function() {
        return delStakeRound(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/stakeRound/export', {
        ...this.queryParams
      }, `stakeRound_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
