<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="订单号" label-width="120px" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" label-width="120px" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="节点ID" prop="nodePlanId">
        <el-input
          v-model="queryParams.nodePlanId"
          placeholder="请输入节点ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="节点等级" label-width="120px" prop="nodePlanLevel">
        <el-select v-model="queryParams.nodePlanLevel" clearable placeholder="请选择节点等级">
          <el-option
            v-for="dict in dict.type.t_node_plan_node_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="支付了多少金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入支付了多少金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="交易hash" label-width="120px" prop="txHash">
        <el-input
          v-model="queryParams.txHash"
          clearable
          placeholder="请输入交易hash"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="业务状态" label-width="120px" prop="bizStatus">
        <el-select v-model="queryParams.bizStatus" clearable placeholder="请选择业务状态">
          <el-option
            v-for="dict in dict.type.t_active_order_biz_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

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

<!--      <el-form-item label="锁定超时时间，默认锁定时间+5分钟" prop="paymentTime">
        <el-date-picker clearable
          v-model="queryParams.paymentTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择锁定超时时间，默认锁定时间+5分钟">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:nodePlanOrder:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >拨付节点</el-button>
      </el-col>-->
<!--
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['xms:nodePlanOrder:edit']"
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
          v-hasPermi="['xms:nodePlanOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:nodePlanOrder:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="nodePlanOrderList" @selection-change="handleSelectionChange">
<!--      <el-table-column align="center" type="selection" width="55" />-->
      <el-table-column v-if="false" align="center" label="主键ID" prop="id"/>
      <el-table-column align="center" label="订单号" prop="orderNo" />
      <el-table-column align="center" label="用户ID" prop="userId" />
<!--      <el-table-column label="节点Id" align="center" prop="nodePlanId" />-->
      <el-table-column align="center" label="节点等级" prop="nodePlanLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_node_plan_node_level" :value="scope.row.nodePlanLevel"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="套餐原价" prop="remark" />
      <el-table-column align="center" label="支付金额" prop="amount" />
      <el-table-column align="center" label="交易hash" prop="txHash" />
      <el-table-column align="center" label="购买地址" prop="address" />
      <el-table-column align="center" label="累计释放年华收益" prop="totalAnnual" />
      <el-table-column align="center" label="年化收益率" prop="annualRate" >
        <template slot-scope="scope">
          <span>{{ scope.row.annualRate}} %</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="业务状态" prop="bizStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_active_order_biz_status" :value="scope.row.bizStatus"/>
        </template>
      </el-table-column>

<!--

      <el-table-column align="center" label="来源类型" prop="sourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_node_plan_order_source_type" :value="scope.row.sourceType"/>
        </template>
      </el-table-column>
-->

      <el-table-column align="center" label="剩余释放本金" prop="haveAmount" />

      <el-table-column align="center" label="剩余释放天数" prop="haveDay" />

      <el-table-column align="center" label="支付时间" prop="paymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
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
            v-hasPermi="['xms:nodePlanOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:nodePlanOrder:remove']"
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

    <!-- 添加或修改用户节点订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="节点id" prop="nodePlanId">
          <el-input v-model="form.nodePlanId" placeholder="请输入节点id" />
        </el-form-item>-->
        <el-form-item label="节点等级" prop="nodePlanLevel">
          <el-select v-model="form.nodePlanLevel" placeholder="请选择节点等级">
            <el-option
              v-for="dict in dict.type.t_node_plan_node_level"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="钱包地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入钱包地址" />
        </el-form-item>
<!--        <el-form-item label="支付了多少金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入支付了多少金额" />
        </el-form-item>
        <el-form-item label="交易hash" prop="txHash">
          <el-input v-model="form.txHash" placeholder="请输入交易hash" />
        </el-form-item>
        <el-form-item label="业务状态 0:待支付,1:已支付,2:释放完成," prop="bizStatus">
          <el-select v-model="form.bizStatus" placeholder="请选择业务状态 0:待支付,1:已支付,2:释放完成,">
            <el-option
              v-for="dict in dict.type.t_active_order_biz_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="锁定超时时间，默认锁定时间+5分钟" prop="paymentTime">
          <el-date-picker clearable
            v-model="form.paymentTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择锁定超时时间，默认锁定时间+5分钟">
          </el-date-picker>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNodePlanOrder, getNodePlanOrder, delNodePlanOrder, addNodePlanOrder, updateNodePlanOrder } from "@/api/xms/nodePlanOrder";

export default {
  name: "NodePlanOrder",
  dicts: ['t_node_plan_node_level', 't_active_order_biz_status','t_node_plan_order_source_type'],
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
      // 用户节点订单表格数据
      nodePlanOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 锁定超时时间，默认锁定时间+5分钟时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        nodePlanId: null,
        nodePlanLevel: null,
        amount: null,
        txHash: null,
        createTime: null,
        bizStatus: null,
        paymentTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        address: [
          { required: true, message: "钱包地址不能为空", trigger: "blur" }
        ],
        nodePlanLevel: [
          { required: true, message: "节点等级不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户节点订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listNodePlanOrder(this.queryParams).then(response => {
        this.nodePlanOrderList = response.rows;
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
        nodePlanId: null,
        nodePlanLevel: null,
        amount: null,
        txHash: null,
        createTime: null,
        address: null,
        bizStatus: null,
        updateTime: null,
        paymentTime: null
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
      this.title = "拨付节点";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNodePlanOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户节点订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNodePlanOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNodePlanOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户节点订单编号为"' + ids + '"的数据项？').then(function() {
        return delNodePlanOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/nodePlanOrder/export', {
        ...this.queryParams
      }, `nodePlanOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
