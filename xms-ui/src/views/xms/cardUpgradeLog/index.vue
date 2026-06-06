<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="用户ID" prop="userId" >
        <el-input
          v-model="queryParams.userId"
          clearable
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="流水类型" label-width="120px" prop="flowType">
        <el-select v-model="queryParams.flowType" clearable placeholder="请选择流水类型">
          <el-option
            v-for="dict in dict.type.t_card_upgrade_log_flow_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="对应的卡片订单号" prop="cardOrderNo">
        <el-input
          v-model="queryParams.cardOrderNo"
          placeholder="请输入对应的卡片订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="卡片唯一标识" label-width="120px" prop="cardSerialNo">
        <el-input
          v-model="queryParams.cardSerialNo"
          clearable
          placeholder="请输入卡片唯一标识"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主订单号" label-width="120px" prop="masterOrderNo">
        <el-input
          v-model="queryParams.masterOrderNo"
          clearable
          placeholder="请输入主订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="子订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入子订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

<!--      <el-form-item label="变更前卡片类型" prop="fromCardType">
        <el-select v-model="queryParams.fromCardType" placeholder="请选择变更前卡片类型" clearable>
          <el-option
            v-for="dict in dict.type.card_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>-->
<!--      <el-form-item label="变更前价格" prop="fromPrice">
        <el-input
          v-model="queryParams.fromPrice"
          placeholder="请输入变更前价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变更前算力" prop="fromPower">
        <el-input
          v-model="queryParams.fromPower"
          placeholder="请输入变更前算力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
<!--      <el-form-item label="变更后卡片类型" prop="toCardType">
        <el-select v-model="queryParams.toCardType" placeholder="请选择变更后卡片类型" clearable>
          <el-option
            v-for="dict in dict.type.card_order_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>-->
<!--      <el-form-item label="变更后价格" prop="toPrice">
        <el-input
          v-model="queryParams.toPrice"
          placeholder="请输入变更后价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变更后算力" prop="toPower">
        <el-input
          v-model="queryParams.toPower"
          placeholder="请输入变更后算力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本次实际支付金额" prop="amountDelta">
        <el-input
          v-model="queryParams.amountDelta"
          placeholder="请输入本次实际支付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本次新增算力" prop="powerDelta">
        <el-input
          v-model="queryParams.powerDelta"
          placeholder="请输入本次新增算力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="创建时间" label-width="150px">
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
          v-hasPermi="['xms:cardUpgradeLog:add']"
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
          v-hasPermi="['xms:cardUpgradeLog:edit']"
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
          v-hasPermi="['xms:cardUpgradeLog:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:cardUpgradeLog:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardUpgradeLogList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键ID" prop="id"/>
      <el-table-column align="center" label="流水类型" prop="flowType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_card_upgrade_log_flow_type" :value="scope.row.flowType"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="对应的卡片订单号" align="center" prop="cardOrderNo" />-->
      <el-table-column align="center" label="卡片唯一标识" prop="cardSerialNo" />
      <el-table-column align="center" label="主订单号" prop="masterOrderNo" />
<!--      <el-table-column label="子订单号" align="center" prop="orderNo" />-->
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="变更前卡片类型" prop="fromCardType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_type" :value="scope.row.fromCardType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变更前价格" prop="fromPrice" />
      <el-table-column align="center" label="变更前算力" prop="fromPower" />
<!--      <el-table-column label="变更前快照" align="center" prop="fromSnapshot" />-->
      <el-table-column align="center" label="变更后卡片类型" prop="toCardType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_type" :value="scope.row.toCardType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="变更后价格" prop="toPrice" />
      <el-table-column align="center" label="变更后算力" prop="toPower" />
      <el-table-column align="center" label="本次实际支付金额" prop="amountDelta" />
      <el-table-column align="center" label="本次新增算力" prop="powerDelta" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:cardUpgradeLog:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:cardUpgradeLog:remove']"
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

    <!-- 添加或修改卡片升级日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流水类型 1-购买 2-升级" prop="flowType">
          <el-select v-model="form.flowType" placeholder="请选择流水类型 1-购买 2-升级">
            <el-option
              v-for="dict in dict.type.t_card_upgrade_log_flow_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="对应的卡片订单号" prop="cardOrderNo">
          <el-input v-model="form.cardOrderNo" placeholder="请输入对应的卡片订单号" />
        </el-form-item>
        <el-form-item label="卡片唯一标识" prop="cardSerialNo">
          <el-input v-model="form.cardSerialNo" placeholder="请输入卡片唯一标识" />
        </el-form-item>
        <el-form-item label="主订单号" prop="masterOrderNo">
          <el-input v-model="form.masterOrderNo" placeholder="请输入主订单号" />
        </el-form-item>
        <el-form-item label="子订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入子订单号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="变更前卡片类型" prop="fromCardType">
          <el-select v-model="form.fromCardType" placeholder="请选择变更前卡片类型">
            <el-option
              v-for="dict in dict.type.card_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="变更前价格" prop="fromPrice">
          <el-input v-model="form.fromPrice" placeholder="请输入变更前价格" />
        </el-form-item>
        <el-form-item label="变更前算力" prop="fromPower">
          <el-input v-model="form.fromPower" placeholder="请输入变更前算力" />
        </el-form-item>
        <el-form-item label="变更后卡片类型" prop="toCardType">
          <el-select v-model="form.toCardType" placeholder="请选择变更后卡片类型">
            <el-option
              v-for="dict in dict.type.card_order_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="变更后价格" prop="toPrice">
          <el-input v-model="form.toPrice" placeholder="请输入变更后价格" />
        </el-form-item>
        <el-form-item label="变更后算力" prop="toPower">
          <el-input v-model="form.toPower" placeholder="请输入变更后算力" />
        </el-form-item>
        <el-form-item label="本次实际支付金额" prop="amountDelta">
          <el-input v-model="form.amountDelta" placeholder="请输入本次实际支付金额" />
        </el-form-item>
        <el-form-item label="本次新增算力" prop="powerDelta">
          <el-input v-model="form.powerDelta" placeholder="请输入本次新增算力" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listCardUpgradeLog, getCardUpgradeLog, delCardUpgradeLog, addCardUpgradeLog, updateCardUpgradeLog } from "@/api/xms/cardUpgradeLog";

export default {
  name: "CardUpgradeLog",
  dicts: ['t_card_upgrade_log_flow_type', 'card_type', 'card_order_type'],
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
      // 卡片升级日志表格数据
      cardUpgradeLogList: [],
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
        flowType: null,
        cardOrderNo: null,
        cardSerialNo: null,
        masterOrderNo: null,
        orderNo: null,
        userId: null,
        fromCardType: null,
        fromPrice: null,
        fromPower: null,
        fromSnapshot: null,
        toCardType: null,
        toPrice: null,
        toPower: null,
        amountDelta: null,
        powerDelta: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        flowType: [
          { required: true, message: "流水类型 1-购买 2-升级不能为空", trigger: "change" }
        ],
        cardOrderNo: [
          { required: true, message: "对应的卡片订单号不能为空", trigger: "blur" }
        ],
        masterOrderNo: [
          { required: true, message: "主订单号不能为空", trigger: "blur" }
        ],
        orderNo: [
          { required: true, message: "子订单号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        fromCardType: [
          { required: true, message: "变更前卡片类型不能为空", trigger: "change" }
        ],
        toCardType: [
          { required: true, message: "变更后卡片类型不能为空", trigger: "change" }
        ],
        toPrice: [
          { required: true, message: "变更后价格不能为空", trigger: "blur" }
        ],
        toPower: [
          { required: true, message: "变更后算力不能为空", trigger: "blur" }
        ],
        amountDelta: [
          { required: true, message: "本次实际支付金额不能为空", trigger: "blur" }
        ],
        powerDelta: [
          { required: true, message: "本次新增算力不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "记录时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询卡片升级日志列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listCardUpgradeLog(this.queryParams).then(response => {
        this.cardUpgradeLogList = response.rows;
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
        flowType: null,
        cardOrderNo: null,
        cardSerialNo: null,
        masterOrderNo: null,
        orderNo: null,
        userId: null,
        fromCardType: null,
        fromPrice: null,
        fromPower: null,
        fromSnapshot: null,
        toCardType: null,
        toPrice: null,
        toPower: null,
        amountDelta: null,
        powerDelta: null,
        createTime: null,
        remark: null
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
      this.title = "添加卡片升级日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCardUpgradeLog(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡片升级日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCardUpgradeLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCardUpgradeLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除卡片升级日志编号为"' + ids + '"的数据项？').then(function() {
        return delCardUpgradeLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/cardUpgradeLog/export', {
        ...this.queryParams
      }, `cardUpgradeLog_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
