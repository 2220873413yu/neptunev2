<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="100px" size="small">
      <el-form-item label="旧系统订单号" prop="oldOrderNo">
        <el-input
          v-model="queryParams.oldOrderNo"
          clearable
          placeholder="请输入旧系统订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="新系统订单号" prop="stakeOrderNo">
        <el-input
          v-model="queryParams.stakeOrderNo"
          clearable
          placeholder="请输入新系统订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钱包地址" prop="account">
        <el-input
          v-model="queryParams.account"
          clearable
          placeholder="请输入钱包地址"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束时间"
          range-separator="-"
          start-placeholder="开始时间"
          style="width: 240px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:oldHToAcpDepositRecord:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="oldHToAcpDepositRecordList">
      <el-table-column align="center" label="旧系统订单号" min-width="170" prop="oldOrderNo" />
      <el-table-column align="center" label="新系统订单号" min-width="170" prop="stakeOrderNo" />
      <el-table-column align="center" label="用户ID" prop="userId" width="110" />
      <el-table-column align="center" label="钱包地址" min-width="180" prop="account" show-overflow-tooltip />
      <el-table-column align="center" label="旧H数量" prop="oldHAmount" width="120" />
      <el-table-column align="center" label="H单价U" width="110">
        <template slot-scope="scope">
          <span>{{ getHPriceUsdtSnapshot(scope.row) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="折U价值" prop="oldHUsdtAmount" width="120" />
      <el-table-column align="center" label="ACP单价U" prop="acpPriceUsdtSnapshot" width="120" />
      <el-table-column align="center" label="ACP入金数量" prop="acpDepositAmount" width="130" />
      <el-table-column align="center" label="状态" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag size="mini" type="success">{{ getStatusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listOldHToAcpDepositRecord } from '@/api/xms/oldHToAcpDepositRecord'

export default {
  name: 'OldHToAcpDepositRecord',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      oldHToAcpDepositRecordList: [],
      daterangeCreateTime: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        oldOrderNo: null,
        stakeOrderNo: null,
        userId: null,
        account: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listOldHToAcpDepositRecord(this.addDateRange(this.queryParams, this.daterangeCreateTime, 'CreateTime')).then(response => {
        this.oldHToAcpDepositRecordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.daterangeCreateTime = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleExport() {
      this.download('xms/oldHToAcpDepositRecord/export', {
        ...this.addDateRange(this.queryParams, this.daterangeCreateTime, 'CreateTime')
      }, `oldHToAcpDepositRecord_${new Date().getTime()}.xlsx`)
    },
    getStatusLabel(value) {
      return value === 1 ? '成功' : value
    },
    getHPriceUsdtSnapshot(row) {
      return row.hpriceUsdtSnapshot || row.hPriceUsdtSnapshot || '-'
    }
  }
}
</script>
