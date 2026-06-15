<template>
  <div class="dashboard-container">
    <div class="dashboard-panel">
      <!-- 顶部概览区域 - 最重要的4个指标 -->
      <el-row :gutter="20" align="stretch" class="mb-10" type="flex">
        <el-col :lg="6" :md="6" :sm="6" :xs="12">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">总计用户</div>
                <div class="overview-value">
                  <count-to :duration='2000' :endVal='truncate4(fromData.userTotal)' :startVal='0' />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :lg="6" :md="6" :sm="6" :xs="12">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-plus"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">今日新增</div>
                <div class="overview-value">
                  <count-to :duration='2000' :endVal='truncate4(fromData.todayUserCount)' :startVal='0' />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :lg="6" :md="6" :sm="6" :xs="12">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">本轮次可提现余额</div>
                <div class="overview-value">
                  <count-to :decimals="6" :duration='2000' :endVal='truncate4(fromData.v7)' :startVal='0' />
                </div>
              </div>

            </div>
          </el-card>
        </el-col>

<!--        <el-col :xs="12" :sm="6" :md="6" :lg="6">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">累计消耗MAI</div>
                <div class="overview-value">
                  <count-to :startVal='0' :endVal='truncate4(fromData.v9)' :decimals="6" :duration='2000' />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

<!--        <el-col :xs="12" :sm="6" :md="6" :lg="6">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">今日销毁BOOMAI</div>
                <div class="overview-value">
                  <count-to :startVal='0' :endVal='truncate4(fromData.v8)' :decimals="6" :duration='2000' />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

<!--        <el-col :xs="12" :sm="6" :md="6" :lg="6">
          <el-card class="overview-card" shadow="hover">
            <div class="overview-content">
              <div class="overview-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="overview-data">
                <div class="overview-label">累计分发BSC->TRX</div>
                <div class="overview-value">
                  <count-to :startVal='0' :endVal='truncate4(fromData.v10)' :duration='2000' />
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->
      </el-row>

      <!-- 主要数据卡片 -->
      <el-row :gutter="20" align="stretch" type="flex">

        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card invest-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-time"></i>
                <span>本轮全网发放数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">工作室补贴</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v46)"
                              :endVal='toNumber(fromData.v46)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">静态收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v47)"
                              :endVal='toNumber(fromData.v47)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">动态收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v48)"
                              :endVal='toNumber(fromData.v48)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网财富仓</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v49)"
                              :endVal='toNumber(fromData.v49)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网入金</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v50)"
                              :endVal='toNumber(fromData.v50)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网购买贡献分消耗ACP</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v44)"
                              :endVal='toNumber(fromData.v44)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网购买节点质押H总量</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v45)"
                              :endVal='toNumber(fromData.v45)'
                              :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 用户数据卡片 -->
        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card user-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-user"></i>
                <span>本轮用户各收益余额</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">全网节点收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v6)"
                              :endVal='toNumber(fromData.v6)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网静态</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v8)"
                              :endVal='toNumber(fromData.v8)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网动态</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v9)"
                              :endVal='toNumber(fromData.v9)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网财富</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v10)"
                              :endVal='toNumber(fromData.v10)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网魔盒</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v11)"
                              :endVal='toNumber(fromData.v11)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网工作室收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v12)"
                              :endVal='toNumber(fromData.v12)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网贡献分</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v13)"
                              :endVal='toNumber(fromData.v13)'
                              :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 待提现数据卡片 -->
<!--        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card invest-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-time"></i>
                <span>待提现数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">节点收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v35)"
                              :endVal='toNumber(fromData.v35)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">静态</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v36)"
                              :endVal='toNumber(fromData.v36)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">动态</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v37)"
                              :endVal='toNumber(fromData.v37)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">财富</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v38)"
                              :endVal='toNumber(fromData.v38)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">魔盒</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v39)"
                              :endVal='toNumber(fromData.v39)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">工作室收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v40)"
                              :endVal='toNumber(fromData.v40)'
                              :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card profit-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-data-line"></i>
                <span>今日数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">今日新增业绩</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v29)"
                              :endVal='toNumber(fromData.v29)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">今日发放工作室收益</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v30)"
                              :endVal='toNumber(fromData.v30)'
                              :duration='2000' />
                  </div>
                </div>

                <div class="data-item">
                  <div class="data-label">今日动态提现总额</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v41)"
                              :endVal='toNumber(fromData.v41)'
                              :duration='2000' />
                  </div>
                </div>

                <div class="data-item">
                  <div class="data-label">今日静态提现总额</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v42)"
                              :endVal='toNumber(fromData.v42)'
                              :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">今日购买贡献分花费ACP</div>
                  <div class="data-value">
                    <count-to :startVal='0'
                              :decimals="getDecimalLen(fromData.v43)"
                              :endVal='toNumber(fromData.v43)'
                              :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 收益数据卡片 -->
<!--        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card profit-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-data-line"></i>
                <span>收益数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">今日全网静态(FSN)</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v14)' :decimals="4" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网静态(FSN)</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v24)' :decimals="4" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">今日全网动态(FSN)</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v15)' :decimals="4" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">全网动态(FSN)</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v25)' :decimals="4" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

        <!-- 提现数据卡片 -->
<!--        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card user-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>XLS提现数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">今日提现</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v18)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">历史提现</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v20)' :decimals="6" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

<!--
        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card invest-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>全网当前资产余额</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">XLS</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v26)' :decimals="6" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
-->

        <!-- FSN提现数据卡片 -->
<!--        <el-col :xs="24" :sm="12" :lg="8" class="mb-10">
          <el-card class="dashboard-card invest-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>BPAY提现数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">今日提现</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v19)' :decimals="4" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">历史提现</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v21)' :decimals="4" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

        <!-- 充值数据 -->
<!--        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card profit-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>USDT充值数据</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">总计充值</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v22)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">今日充值</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v23)' :decimals="6" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

        <!--   全网当前资产余额     -->
<!--        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card user-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>全网当前资产余额</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">USDT</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v26)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">Bpay</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v27)' :decimals="6" :duration='2000' />
                  </div>
                </div>
&lt;!&ndash;                <div class="data-item">
                  <div class="data-label">佣金钱包</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v28)' :decimals="4" :duration='2000' />
                  </div>
                </div>&ndash;&gt;
              </div>
            </div>
          </el-card>
        </el-col>-->


      </el-row>

      <!-- 第二行数据卡片 -->
<!--      <el-row :gutter="20" type="flex" align="stretch">
        &lt;!&ndash;   全网拨款信息     &ndash;&gt;
        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card invest-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>全网当前资产余额</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">USDT</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v26)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">Bpay</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v27)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">佣金钱包</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v31)' :decimals="4" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->



        <!--   全网扣款信息     -->
<!--        <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8" class="mb-10">
          <el-card class="dashboard-card profit-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <i class="el-icon-bottom"></i>
                <span>全网扣款信息</span>
              </div>
            </template>
            <div class="card-content">
              <div class="data-list">
                <div class="data-item">
                  <div class="data-label">BOOMAI</div>
                  <div class="data-value highlight">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v32)' :decimals="6" :duration='2000' />
                  </div>
                </div>
                <div class="data-item">
                  <div class="data-label">MAI</div>
                  <div class="data-value">
                    <count-to :startVal='0' :endVal='truncate4(fromData.v33)' :decimals="6" :duration='2000' />
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>-->

<!--      </el-row>-->
    </div>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import {getUserDataBoard} from '@/api/xms/statistics';
export default {
  components: {
    CountTo
  },
  data() {
    return {
      fromData: {
        userTotal: 0,
        todayUserCount: 0,
        v6: 0,
        v7: 0,
        v8: 0,
        v9: 0,
        v10: 0,
        v11: 0,
        v12: 0,
        v13: 0,
        v35: 0,
        v36: 0,
        v37: 0,
        v38: 0,
        v39: 0,
        v40: 0,
        v18: 0,
        v20: 0,
        v22: 0,
        v23: 0,
        v26: 0,
        v27: 0,
        v29: 0,
        v30: 0,
        v32: 0,
        v33: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      getUserDataBoard().then(response => {
        this.fromData = response.data
      })
    },
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    getDecimalLen(val) {
      if (val === null || val === undefined) {
        return 0
      }
      const str = String(val)
      const dotIndex = str.indexOf('.')
      if (dotIndex < 0) {
        return 0
      }
      return str.length - dotIndex - 1
    },
    toNumber(val) {
      if (val === null || val === undefined || val === '') {
        return 0
      }
      const num = Number(val)
      return isNaN(num) ? 0 : num
    },
    // 新增截断方法
    truncate4(val) {
      if (typeof val !== 'number') val = Number(val)
      if (isNaN(val)) return 0
      const factor = 10000
      return val >= 0
        ? Math.floor(val * factor) / factor
        : Math.ceil(val * factor) / factor
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 16px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  min-height: auto;
}

.dashboard-panel {
  padding: 8px 0;
}

.mb-10 {
  margin-bottom: 20px;
}

// 顶部概览卡片
.overview-card {
  height: 100px;
  max-height: 100px;
  border-radius: 8px;
  transition: all 0.3s;
  cursor: pointer;
  border: none;
  background: linear-gradient(135deg, rgba(255,255,255,0.95) 0%, rgba(255,255,255,0.8) 100%);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
  }

  .overview-content {
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 15px;

    .overview-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      background: linear-gradient(135deg, #409EFF 0%, #64B5F6 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      box-shadow: 0 4px 10px rgba(64, 158, 255, 0.2);

      i {
        color: white;
        font-size: 24px;
      }
    }

    .overview-data {
      flex: 1;

      .overview-label {
        font-size: 14px;
        color: #606266;
        margin-bottom: 5px;
      }

      .overview-value {
        font-size: 22px;
        font-weight: 600;
        color: #303133;
      }
    }
  }

  &:nth-child(1) .overview-icon {
    background: linear-gradient(135deg, #67C23A 0%, #95D475 100%);
    box-shadow: 0 4px 10px rgba(103, 194, 58, 0.2);
  }

  &:nth-child(2) .overview-icon {
    background: linear-gradient(135deg, #E6A23C 0%, #F5CD79 100%);
    box-shadow: 0 4px 10px rgba(230, 162, 60, 0.2);
  }

  &:nth-child(3) .overview-icon {
    background: linear-gradient(135deg, #409EFF 0%, #64B5F6 100%);
    box-shadow: 0 4px 10px rgba(64, 158, 255, 0.2);
  }

  &:nth-child(4) .overview-icon {
    background: linear-gradient(135deg, #F56C6C 0%, #FFA9A9 100%);
    box-shadow: 0 4px 10px rgba(245, 108, 108, 0.2);
  }
}

// 数据卡片
.dashboard-card {
  min-height: 180px;
  border-radius: 8px;
  transition: all 0.3s;
  position: relative;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  }

  .card-header {
    display: flex;
    align-items: center;
    font-size: 16px;
    padding: 12px 15px;
    font-weight: 600;
    border-bottom: 1px solid #f0f0f0;

    i {
      margin-right: 8px;
      font-size: 18px;
    }
  }

  .card-content {
    padding: 15px;
  }

  .data-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
    min-height: 80px;
  }

  .data-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 12px;
    border-radius: 8px;
    background-color: #f8f9fa;
    border-left: 3px solid transparent;
    transition: all 0.3s;
    flex: 1;

    &:hover {
      background-color: #f0f2f5;
      border-left-color: #409EFF;
      transform: translateX(3px);
    }

    .data-label {
      color: #606266;
      font-size: 14px;
      font-weight: 500;
    }

    .data-value {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      word-break: break-all;
      text-align: right;

      &.highlight {
        color: #409EFF;
        font-size: 17px;
      }
    }
  }
}

// 不同卡片类型的样式
.user-card {
  border-top: 3px solid #67C23A;
  .card-header {
    color: #67C23A;
  }
}

.invest-card {
  border-top: 3px solid #409EFF;
  .card-header {
    color: #409EFF;
  }
}

.profit-card {
  border-top: 3px solid #F56C6C;
  .card-header {
    color: #F56C6C;
  }
}

.withdraw-card {
  border-top: 3px solid #E6A23C;
  .card-header {
    color: #E6A23C;
  }
}

.fsn-withdraw-card {
  border-top: 3px solid #909399;
  .card-header {
    color: #909399;
  }
}

// 简化数字增长动画效果
.count-to {
  display: inline-block;
}

// 响应式优化
@media (max-width: 768px) {
  .dashboard-container {
    padding: 8px;
  }

  .dashboard-panel {
    padding: 4px 0;
  }

  .overview-card {
    margin-bottom: 10px;

    .overview-content {
      padding: 0 10px;

      .overview-icon {
        width: 50px;
        height: 50px;
        margin-right: 10px;

        i {
          font-size: 20px;
        }
      }

      .overview-data {
        .overview-label {
          font-size: 12px;
        }

        .overview-value {
          font-size: 18px;
        }
      }
    }
  }

  .dashboard-card {
    margin-bottom: 15px;
    min-height: 160px;

    .card-header {
      padding: 10px 12px;
      font-size: 14px;

      i {
        font-size: 16px;
      }
    }

    .card-content {
      padding: 12px;
    }

    .data-item {
      padding: 12px 10px;

      .data-label {
        font-size: 13px;
      }

      .data-value {
        font-size: 14px;

        &.highlight {
          font-size: 15px;
        }
      }
    }
  }
}

@media (min-width: 769px) and (max-width: 992px) {
  .dashboard-container {
    padding: 12px;
  }

  .overview-card {
    .overview-content {
      .overview-icon {
        width: 55px;
        height: 55px;

        i {
          font-size: 22px;
        }
      }

      .overview-data {
        .overview-value {
          font-size: 20px;
        }
      }
    }
  }
}

@media (min-width: 1200px) {
  .dashboard-container {
    padding: 20px;
  }

  .dashboard-card {
    min-height: 200px;
  }
}

// 大屏幕下优化3列布局的间距
@media (min-width: 1600px) {
  .dashboard-container {
    padding: 24px;
  }

  .el-row {
    margin-left: -12px !important;
    margin-right: -12px !important;
  }

  .el-col {
    padding-left: 12px !important;
    padding-right: 12px !important;
  }
}
</style>
