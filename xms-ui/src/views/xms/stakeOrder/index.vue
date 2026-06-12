<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo" label-width="120px">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
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

      <el-form-item label="用户ID" prop="userId" label-width="120px">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="业绩归属上级ID" prop="belongUserId" label-width="120px">
        <el-input
          v-model="queryParams.belongUserId"
          placeholder="请输入业绩归属上级ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="入金来源" prop="depositSourceType" label-width="120px">
        <el-select v-model="queryParams.depositSourceType" placeholder="请选择入金来源" clearable>
          <el-option
            v-for="item in depositSourceTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="本次质押金额(单位H)" prop="stakeAmount">
        <el-input
          v-model="queryParams.stakeAmount"
          placeholder="请输入本次质押金额(单位H)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="链上交易hash" prop="txHash" label-width="120px">
        <el-input
          v-model="queryParams.txHash"
          placeholder="请输入链上交易hash"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="成功时间" prop="successTime">
        <el-date-picker clearable
          v-model="queryParams.successTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择成功时间">
        </el-date-picker>
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
          v-hasPermi="['xms:stakeOrder:add']"
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
          v-hasPermi="['xms:stakeOrder:edit']"
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
          v-hasPermi="['xms:stakeOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:stakeOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stakeOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="钱包地址" align="center" prop="userAccount" />
      <el-table-column label="业绩归属上级ID" align="center" prop="belongUserId" />

      <el-table-column label="轮次编号" align="center" prop="stakeRoundId" />
      <el-table-column label="入金来源" align="center" prop="depositSourceType" width="150">
        <template slot-scope="scope">
          <span>{{ formatDepositSourceType(scope.row.depositSourceType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ACP入金数量" align="center" prop="stakeAmount" width="130">
        <template slot-scope="scope">
          <span>{{ scope.row.stakeAmount }} ACP</span>
        </template>
      </el-table-column>
      <el-table-column label="旧系统入金H数量" align="center" prop="oldHAmount" width="150">
        <template slot-scope="scope">
          <span>{{ formatOldHAmount(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="H单价U" align="center" width="110">
        <template slot-scope="scope">
          <span>{{ getHPriceUsdtSnapshot(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="ACP单价U" align="center" prop="acpPriceUsdtSnapshot" width="120" />

      <el-table-column align="center" label="订单状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_stake_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="hash" align="center" prop="txHash" />
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column label="创建日期" align="center" prop="createDay" />
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
            v-hasPermi="['xms:stakeOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:stakeOrder:remove']"
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

    <!-- 添加或修改质押订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号(唯一)" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号(唯一)" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="轮次表id" prop="stakeRoundId">
          <el-input v-model="form.stakeRoundId" placeholder="请输入轮次表id" />
        </el-form-item>
        <el-form-item label="ACP入金数量" prop="stakeAmount">
          <el-input v-model="form.stakeAmount" placeholder="请输入ACP入金数量" />
        </el-form-item>
        <el-form-item label="链上交易hash" prop="txHash">
          <el-input v-model="form.txHash" placeholder="请输入链上交易hash" />
        </el-form-item>
        <el-form-item label="成功时间" prop="successTime">
          <el-date-picker clearable
            v-model="form.successTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择成功时间">
          </el-date-picker>
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
import { listStakeOrder, getStakeOrder, delStakeOrder, addStakeOrder, updateStakeOrder } from "@/api/xms/stakeOrder";

export default {
  name: "StakeOrder",
  dicts: ['t_stake_order_status'],
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
      // 质押订单表格数据
      stakeOrderList: [],
      // 入金来源选项
      depositSourceTypeOptions: [
        { value: 1, label: "正常ACP入金" },
        { value: 3, label: "旧系统H换ACP入金" }
      ],
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
        belongUserId: null,
        orderNo: null,
        userAccount: null,
        userId: null,
        stakeRoundId: null,
        stakeAmount: null,
        depositSourceType: null,
        status: null,
        txHash: null,
        successTime: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单号(唯一)不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        stakeRoundId: [
          { required: true, message: "轮次表id不能为空", trigger: "blur" }
        ],
        stakeAmount: [
          { required: true, message: "ACP入金数量不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态:1成功,2:未处理不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询质押订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listStakeOrder(this.queryParams).then(response => {
        this.stakeOrderList = response.rows;
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
        orderNo: null,
        userId: null,
        stakeRoundId: null,
        stakeAmount: null,
        oldHAmount: null,
        depositSourceType: null,
        acpPriceUsdtSnapshot: null,
        hpriceUsdtSnapshot: null,
        status: null,
        txHash: null,
        successTime: null,
        remark: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    formatDepositSourceType(value) {
      const option = this.depositSourceTypeOptions.find(item => item.value === Number(value));
      return option ? option.label : value;
    },
    formatOldHAmount(row) {
      if (Number(row.depositSourceType) !== 3) {
        return "-";
      }
      return `${row.oldHAmount || 0} H`;
    },
    getHPriceUsdtSnapshot(row) {
      return row.hpriceUsdtSnapshot || row.hPriceUsdtSnapshot || "-";
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
      this.title = "添加质押订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStakeOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改质押订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStakeOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStakeOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除质押订单编号为"' + ids + '"的数据项？').then(function() {
        return delStakeOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/stakeOrder/export', {
        ...this.queryParams
      }, `stakeOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
