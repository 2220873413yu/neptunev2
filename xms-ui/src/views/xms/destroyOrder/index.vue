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

      <el-form-item label="交易hash" label-width="120px" prop="hash">
        <el-input
          v-model="queryParams.hash"
          clearable
          placeholder="请输入交易hash"
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

      <el-form-item label="钱包地址" label-width="120px" prop="userAccount">
        <el-input
          v-model="queryParams.userAccount"
          clearable maxlength="120"
          placeholder="请输入钱包地址"
          show-word-limit
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
      <el-form-item label="价值多少u" prop="usdtValue">
        <el-input
          v-model="queryParams.usdtValue"
          placeholder="请输入价值多少u"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="销毁了多少个boomai" prop="validNum1Value">
        <el-input
          v-model="queryParams.validNum1Value"
          placeholder="请输入销毁了多少个boomai"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="valid_num1价格" prop="lastFtnPrice">
        <el-input
          v-model="queryParams.lastFtnPrice"
          placeholder="请输入valid_num1价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已经获取了多少个boomai" prop="haveValidNum1">
        <el-input
          v-model="queryParams.haveValidNum1"
          placeholder="请输入已经获取了多少个boomai"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="订单状态" label-width="120px" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择订单状态">
          <el-option
            v-for="dict in dict.type.t_destroy_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="支付状态" label-width="120px" prop="payStatus">
        <el-select v-model="queryParams.payStatus" clearable placeholder="请选择支付状态">
          <el-option
            v-for="dict in dict.type.t_destroy_order_pay_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="是否减产" label-width="120px" prop="isReduced">
        <el-select v-model="queryParams.isReduced" clearable placeholder="请选择是否减产">
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
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
<!--      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付时间">
        </el-date-picker>
      </el-form-item>-->
<!--      <el-form-item label="业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务" prop="bizStatus">
        <el-select v-model="queryParams.bizStatus" placeholder="请选择业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务" clearable>
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否已经加速 0:否,1:是" prop="isReduced">
        <el-input
          v-model="queryParams.isReduced"
          placeholder="请输入是否已经加速 0:否,1:是"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['xms:destroyOrder:add']"
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
          v-hasPermi="['xms:destroyOrder:edit']"
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
          v-hasPermi="['xms:destroyOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:destroyOrder:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="destroyOrderList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="钱包地址" prop="userAccount" />
      <el-table-column align="center" label="订单号" prop="orderNo" />
      <el-table-column align="center" label="交易hash" prop="hash" show-overflow-tooltip width="180"/>
      <el-table-column align="center" label="质押周期" prop="days">
        <template slot-scope="scope">
          <span>{{ scope.row.days }} 天</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="销毁支出(U)" prop="usdtValue" />

      <el-table-column align="center" label="销毁 BOOMAI 数量" prop="validNum1Value" />
      <el-table-column align="center" label="销毁时 BOOMAI 单价(U)" prop="lastFtnPrice" />
      <el-table-column align="center" label="已释放 BOOMAI" prop="haveValidNum1" />
      <el-table-column align="center" label="支付状态" prop="payStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_destroy_order_pay_status" :value="scope.row.payStatus"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="订单状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_destroy_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否减产" prop="isReduced">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.isReduced"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="减产目标(BOOMAI)" prop="reduceTargetAmount" />
<el-table-column align="center" label="累计产出(BOOMAI)" prop="releaseAccumulate" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="支付时间" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
<!--      <el-table-column label="业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务" align="center" prop="bizStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.bizStatus"/>
        </template>
      </el-table-column>-->

<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:destroyOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:destroyOrder:remove']"
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

    <!-- 添加或修改销毁记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="矿机天数" prop="days">
          <el-input v-model="form.days" placeholder="请输入矿机天数" />
        </el-form-item>
        <el-form-item label="价值多少u" prop="usdtValue">
          <el-input v-model="form.usdtValue" placeholder="请输入价值多少u" />
        </el-form-item>
        <el-form-item label="销毁了多少个boomai" prop="validNum1Value">
          <el-input v-model="form.validNum1Value" placeholder="请输入销毁了多少个boomai" />
        </el-form-item>
        <el-form-item label="valid_num1价格" prop="lastFtnPrice">
          <el-input v-model="form.lastFtnPrice" placeholder="请输入valid_num1价格" />
        </el-form-item>
        <el-form-item label="已经获取了多少个boomai" prop="haveValidNum1">
          <el-input v-model="form.haveValidNum1" placeholder="请输入已经获取了多少个boomai" />
        </el-form-item>
        <el-form-item label="订单状态 0:待支付,1:运行中,2:暂停" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.t_destroy_order_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker v-model="form.payTime"
            clearable
            placeholder="请选择支付时间"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入内容" type="textarea" />
        </el-form-item>
        <el-form-item label="业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务" prop="bizStatus">
          <el-radio-group v-model="form.bizStatus">
            <el-radio
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否已经加速 0:否,1:是" prop="isReduced">
          <el-input v-model="form.isReduced" placeholder="请输入是否已经加速 0:否,1:是" />
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
import { listDestroyOrder, getDestroyOrder, delDestroyOrder, addDestroyOrder, updateDestroyOrder } from "@/api/xms/destroyOrder";

export default {
  name: "DestroyOrder",
  dicts: ['t_destroy_order_status', 't_user_info_is_valid','t_destroy_order_pay_status'],
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
      // 销毁记录表格数据
      destroyOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否删除时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        userId: null,
        hash: null,
        days: null,
        userAccount: null,
        payStatus: null,
        usdtValue: null,
        validNum1Value: null,
        lastFtnPrice: null,
        haveValidNum1: null,
        status: null,
        createTime: null,
        payTime: null,
        bizStatus: null,
        isReduced: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
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
    /** 查询销毁记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' !== this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listDestroyOrder(this.queryParams).then(response => {
        this.destroyOrderList = response.rows;
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
        days: null,
        usdtValue: null,
        validNum1Value: null,
        lastFtnPrice: null,
        haveValidNum1: null,
        status: null,
        createTime: null,
        payTime: null,
        updateTime: null,
        remark: null,
        bizStatus: null,
        isReduced: null
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
      this.title = "添加销毁记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDestroyOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改销毁记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDestroyOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDestroyOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除销毁记录编号为"' + ids + '"的数据项？').then(function() {
        return delDestroyOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/destroyOrder/export', {
        ...this.queryParams
      }, `destroyOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
