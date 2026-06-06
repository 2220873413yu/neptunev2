<template>
  <div class="destroy-stat-page">
    <div class="section-block">
      <div class="section-title">销毁订单统计概览</div>
      <el-row :gutter="20">
        <el-col :sm="6" :xs="12">
          <div class="summary-card">
            <div class="label">累计订单</div>
            <div class="value">{{ formatNumber(orderSummary.totalOrders) }}</div>
            <div class="desc">含运行 / 已减产</div>
          </div>
        </el-col>
        <el-col :sm="6" :xs="12">
          <div class="summary-card">
            <div class="label">运行中</div>
            <div class="value success">{{ formatNumber(orderSummary.runningOrders) }}</div>
            <div class="desc">销毁 {{ formatNumber(orderSummary.runningDestroyAmount, { decimals: 2 }) }} BOOMAI</div>
          </div>
        </el-col>
        <el-col :sm="6" :xs="12">
          <div class="summary-card">
            <div class="label">已减产</div>
            <div class="value warning">{{ formatNumber(orderSummary.reducedOrders) }}</div>
            <div class="desc">完成度 ≥ 100%</div>
          </div>
        </el-col>
        <el-col :sm="6" :xs="12">
          <div class="summary-card">
            <div class="label">待支付/关闭</div>
            <div class="value danger">{{ formatNumber(orderSummary.pendingOrders) }}</div>
            <div class="desc">需跟进处理</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-row :gutter="20">
      <el-col :md="12" :xs="24">
        <div class="chart-card">
          <div class="chart-title">近7天新建订单趋势</div>
          <line-chart :chart-data="trendData" height="320px" />
        </div>
      </el-col>
      <el-col :md="12" :xs="24">
        <div class="chart-card">
          <div class="chart-title">减产状态</div>
          <pie-chart :chart-data="reduceData" height="320px" />
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :md="24" :xs="24">
        <div class="chart-card">
          <div class="chart-title">销毁金额区间</div>
          <line-chart :chart-data="amountChartData" height="320px" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LineChart from '@/views/dashboard/LineChart'
import PieChart from '@/views/dashboard/PieChart'
import { getDestroyOrderStatistics } from '@/api/xms/destroyOrder'

export default {
  name: 'DestroyStat',
  components: { LineChart, PieChart },
  data() {
    return {
      orderSummary: {
        totalOrders: 0,
        runningOrders: 0,
        runningDestroyAmount: 0,
        reducedOrders: 0,
        pendingOrders: 0
      },
      trendData: {
        expectedData: [],
        key: [],
        seriesName: '新增订单数'
      },
      reduceData: {
        legend: ['未减产', '已减产'],
        seriesData: [
          { name: '未减产', value: 0 },
          { name: '已减产', value: 0 }
        ]
      },
      amountChartData: {
        expectedData: [],
        key: [],
        seriesName: '订单数量'
      }
    }
  },
  created() {
    this.loadDestroyStats()
  },
  methods: {
    loadDestroyStats() {
      getDestroyOrderStatistics().then(res => {
        const data = res.data || {}
        if (data.summary) {
          this.orderSummary = Object.assign({}, this.orderSummary, data.summary)
        }
        if (data.trend) {
          this.trendData = Object.assign({}, this.trendData, data.trend)
        }
        if (data.reduceStatus) {
          this.reduceData = Object.assign({}, this.reduceData, data.reduceStatus)
        }
        if (data.amount) {
          this.amountChartData = Object.assign({}, this.amountChartData, data.amount)
        }
      })
    },
    formatNumber(val, options = {}) {
      if (val === undefined || val === null || val === '') {
        return options.keepZero ? '0' : 0
      }
      const num = Number(val)
      if (Number.isNaN(num)) {
        return val
      }
      const decimals = options.decimals !== undefined ? options.decimals : 0
      return num.toLocaleString(undefined, {
        minimumFractionDigits: decimals,
        maximumFractionDigits: decimals
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.destroy-stat-page {
  padding: 24px;
  background: #f5f7fa;

  .section-block {
    margin-bottom: 24px;
  }

  .section-title {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 16px;
  }

  .summary-card {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    margin-bottom: 16px;

    .label {
      font-size: 14px;
      color: #909399;
    }
    .value {
      font-size: 30px;
      font-weight: 600;
      margin: 6px 0 4px;
      color: #303133;

      &.success { color: #67c23a; }
      &.warning { color: #e6a23c; }
      &.danger { color: #f56c6c; }
    }
    .desc {
      font-size: 12px;
      color: #c0c4cc;
    }
  }

  .chart-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    .chart-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
    }
  }
}
</style>

