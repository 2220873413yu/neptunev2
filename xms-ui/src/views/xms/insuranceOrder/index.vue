<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="矿机天数" prop="days">
        <el-input
          v-model="queryParams.days"
          placeholder="请输入矿机天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="剩余天数" prop="haveDays">
        <el-input
          v-model="queryParams.haveDays"
          placeholder="请输入剩余天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日产出" prop="dayOutReward">
        <el-input
          v-model="queryParams.dayOutReward"
          placeholder="请输入日产出"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="保险仓余额" prop="insuranceBalance">
        <el-input
          v-model="queryParams.insuranceBalance"
          placeholder="请输入保险仓余额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="剩余保险仓产出" prop="hsaveInsuranceBalance">
        <el-input
          v-model="queryParams.hsaveInsuranceBalance"
          placeholder="请输入剩余保险仓产出"
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
      <el-form-item label="轮次表ID" prop="stakeRoundId" label-width="120px">
        <el-input
          v-model="queryParams.stakeRoundId"
          placeholder="请输入轮次表ID"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"

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
          v-hasPermi="['xms:insuranceOrder:add']"
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
          v-hasPermi="['xms:insuranceOrder:edit']"
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
          v-hasPermi="['xms:insuranceOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:insuranceOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="insuranceOrderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" v-if="false"/>
      <el-table-column label="主键id" align="center" prop="id" v-if="false"/>
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="矿机天数" align="center" prop="days" />
      <el-table-column label="剩余天数" align="center" prop="haveDays" />
      <el-table-column label="日产出" align="center" prop="dayOutReward" />
      <el-table-column label="保险仓余额" align="center" prop="insuranceBalance" />
      <el-table-column label="剩余保险仓产出" align="center" prop="hsaveInsuranceBalance" />
      <el-table-column label="订单状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span>{{ scope.row.status === 0 ? '释放中' : scope.row.status === 1 ? '已结束' : '' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="轮次表ID" align="center" prop="stakeRoundId" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="remark" align="center" prop="remark" />-->

<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:insuranceOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:insuranceOrder:remove']"
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

    <!-- 添加或修改保险仓释放订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="矿机天数" prop="days">
          <el-input v-model="form.days" placeholder="请输入矿机天数" />
        </el-form-item>
        <el-form-item label="剩余天数" prop="haveDays">
          <el-input v-model="form.haveDays" placeholder="请输入剩余天数" />
        </el-form-item>
        <el-form-item label="日产出" prop="dayOutReward">
          <el-input v-model="form.dayOutReward" placeholder="请输入日产出" />
        </el-form-item>
        <el-form-item label="保险仓余额" prop="insuranceBalance">
          <el-input v-model="form.insuranceBalance" placeholder="请输入保险仓余额" />
        </el-form-item>
        <el-form-item label="剩余保险仓产出" prop="hsaveInsuranceBalance">
          <el-input v-model="form.hsaveInsuranceBalance" placeholder="请输入剩余保险仓产出" />
        </el-form-item>
        <el-form-item label="remark" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入remark" />
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
import { listInsuranceOrder, getInsuranceOrder, delInsuranceOrder, addInsuranceOrder, updateInsuranceOrder } from "@/api/xms/insuranceOrder";

export default {
  name: "InsuranceOrder",
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
      // 保险仓释放订单表格数据
      insuranceOrderList: [],
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
        orderNo: null,
        days: null,
        haveDays: null,
        dayOutReward: null,
        insuranceBalance: null,
        hsaveInsuranceBalance: null,
        createTime: null,
        status: null,
        stakeRoundId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        days: [
          { required: true, message: "矿机天数不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询保险仓释放订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listInsuranceOrder(this.queryParams).then(response => {
        this.insuranceOrderList = response.rows;
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
        days: null,
        haveDays: null,
        dayOutReward: null,
        insuranceBalance: null,
        hsaveInsuranceBalance: null,
        createTime: null,
        updateTime: null,
        remark: null,
        status: null,
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
      this.title = "添加保险仓释放订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInsuranceOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改保险仓释放订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInsuranceOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInsuranceOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除保险仓释放订单编号为"' + ids + '"的数据项？').then(function() {
        return delInsuranceOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/insuranceOrder/export', {
        ...this.queryParams
      }, `insuranceOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
