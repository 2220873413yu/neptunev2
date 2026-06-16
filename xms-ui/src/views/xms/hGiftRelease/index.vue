<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="100px" size="small">
      <el-form-item label="释放桶编号" prop="bucketNo">
        <el-input
          v-model="queryParams.bucketNo"
          clearable
          placeholder="请输入释放桶编号"
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
      <el-form-item label="来源类型" prop="sourceType">
        <el-select v-model="queryParams.sourceType" clearable placeholder="请选择来源类型">
          <el-option
            v-for="item in sourceTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="来源订单号" prop="sourceOrderNo">
        <el-input
          v-model="queryParams.sourceOrderNo"
          clearable
          placeholder="请输入来源订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
          v-hasPermi="['xms:hGiftRelease:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:hGiftRelease:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="hGiftReleaseList">
      <el-table-column align="center" label="释放桶编号" min-width="170" prop="bucketNo" />
      <el-table-column align="center" label="用户ID" prop="userId" width="110" />
      <el-table-column align="center" label="钱包地址" min-width="180" prop="account" show-overflow-tooltip />
      <el-table-column align="center" label="来源类型" prop="sourceType" width="120">
        <template slot-scope="scope">
          <span>{{ getSourceTypeLabel(scope.row.sourceType) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="来源订单号" min-width="170" prop="sourceOrderNo" />
      <el-table-column align="center" label="总量(H)" prop="totalAmount" width="120" />
      <el-table-column align="center" label="已释放(H)" prop="releasedAmount" width="120" />
      <el-table-column align="center" label="剩余(H)" prop="remainingAmount" width="120" />
      <el-table-column align="center" label="每日释放(H)" prop="dailyReleaseAmount" width="130" />
      <el-table-column align="center" label="释放天数" prop="releaseDays" width="100" />
      <el-table-column align="center" label="已释放天数" prop="releasedDays" width="110" />
      <el-table-column align="center" label="状态" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)" size="mini">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" fixed="right" label="操作" width="160">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 1"
            v-hasPermi="['xms:hGiftRelease:freeze']"
            icon="el-icon-lock"
            size="mini"
            type="text"
            @click="handleFreeze(scope.row)"
          >冻结</el-button>
          <el-button
            v-if="scope.row.status === 3"
            v-hasPermi="['xms:hGiftRelease:unfreeze']"
            icon="el-icon-unlock"
            size="mini"
            type="text"
            @click="handleUnfreeze(scope.row)"
          >解冻</el-button>
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

    <el-dialog :title="title" :visible.sync="open" append-to-body width="520px">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="钱包地址" prop="account">
          <el-input v-model="form.account" clearable placeholder="请输入钱包地址" />
        </el-form-item>
        <el-form-item label="赠送H总量" prop="totalAmount">
          <el-input v-model="form.totalAmount" clearable placeholder="请输入赠送H总量" />
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
import {
  addHGiftRelease,
  freezeHGiftRelease,
  listHGiftRelease,
  unfreezeHGiftRelease
} from '@/api/xms/hGiftRelease'

export default {
  name: 'HGiftRelease',
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      hGiftReleaseList: [],
      title: '',
      open: false,
      daterangeCreateTime: [],
      sourceTypeOptions: [
        { value: 1, label: '正常ACP入金' },
        { value: 2, label: '后台手动拨' },
        { value: 3, label: '旧系统入金' },
        { value: 4, label: '用户H余额换ACP入金' }
      ],
      statusOptions: [
        { value: 1, label: '释放中' },
        { value: 2, label: '已完成' },
        { value: 3, label: '冻结' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bucketNo: null,
        userId: null,
        account: null,
        sourceType: null,
        sourceOrderNo: null,
        status: null
      },
      form: {},
      rules: {
        account: [
          { required: true, message: '钱包地址不能为空', trigger: 'blur' }
        ],
        totalAmount: [
          { required: true, message: '赠送H总量不能为空', trigger: 'blur' },
          { pattern: /^(0|[1-9]\d*)(\.\d+)?$/, message: '赠送H总量必须大于0', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listHGiftRelease(this.addDateRange(this.queryParams, this.daterangeCreateTime, 'CreateTime')).then(response => {
        this.hGiftReleaseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        account: null,
        totalAmount: null
      }
      this.resetForm('form')
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
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '新增H赠送释放'
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        if (Number(this.form.totalAmount) <= 0) {
          this.$modal.msgError('赠送H总量必须大于0')
          return
        }
        addHGiftRelease(this.form).then(() => {
          this.$modal.msgSuccess('新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    handleFreeze(row) {
      this.$modal.confirm('确认冻结该H赠送释放桶吗？').then(function() {
        return freezeHGiftRelease(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('冻结成功')
      }).catch(() => {})
    },
    handleUnfreeze(row) {
      this.$modal.confirm('确认解冻该H赠送释放桶吗？').then(function() {
        return unfreezeHGiftRelease(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('解冻成功')
      }).catch(() => {})
    },
    handleExport() {
      this.download('xms/hGiftRelease/export', {
        ...this.addDateRange(this.queryParams, this.daterangeCreateTime, 'CreateTime')
      }, `hGiftRelease_${new Date().getTime()}.xlsx`)
    },
    getSourceTypeLabel(value) {
      const item = this.sourceTypeOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    getStatusLabel(value) {
      const item = this.statusOptions.find(option => option.value === value)
      return item ? item.label : value
    },
    getStatusTagType(value) {
      const typeMap = {
        1: '',
        2: 'success',
        3: 'warning'
      }
      return typeMap[value] || ''
    }
  }
}
</script>
