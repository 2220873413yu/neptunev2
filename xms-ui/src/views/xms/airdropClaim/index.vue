<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="领取流水号" label-width="120px" prop="claimNo">
        <el-input
          v-model="queryParams.claimNo"
          clearable
          placeholder="请输入领取流水号/锁定号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轮次编号" label-width="120px" prop="roundNo">
        <el-input
          v-model="queryParams.roundNo"
          clearable
          placeholder="请输入轮次编号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" label-width="120px" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付状态" label-width="120px" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_airdrop_claim_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="锁定超时时间，默认锁定时间+5分钟" prop="lockExpireAt">
        <el-date-picker clearable
          v-model="queryParams.lockExpireAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择锁定超时时间，默认锁定时间+5分钟">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="本次应发放的代币数量" prop="tokenAmount">
        <el-input
          v-model="queryParams.tokenAmount"
          placeholder="请输入本次应发放的代币数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="链上交易哈希" label-width="120px" prop="txHash">
        <el-input
          v-model="queryParams.txHash"
          clearable
          placeholder="请输入链上交易哈希"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="支付币种" prop="payToken">
        <el-input
          v-model="queryParams.payToken"
          placeholder="请输入支付币种"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付金额" prop="payAmount">
        <el-input
          v-model="queryParams.payAmount"
          placeholder="请输入支付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="锁定时间" prop="lockedAt">
        <el-date-picker clearable
          v-model="queryParams.lockedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择锁定时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="终态时间" prop="completedAt">
        <el-date-picker clearable
          v-model="queryParams.completedAt"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择终态时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="超时/取消/失败原因" prop="releaseReason">
        <el-input
          v-model="queryParams.releaseReason"
          placeholder="请输入超时/取消/失败原因"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
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
          v-hasPermi="['xms:airdropClaim:add']"
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
          v-hasPermi="['xms:airdropClaim:edit']"
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
          v-hasPermi="['xms:airdropClaim:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:airdropClaim:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="airdropClaimList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键" prop="id"/>
      <el-table-column align="center" label="流水号" prop="claimNo" />
      <el-table-column align="center" label="空投编号" prop="roundNo" />
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="支付状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_airdrop_claim_status" :value="scope.row.status"/>
        </template>
      </el-table-column>

      <el-table-column align="center" label="领取数量" prop="tokenAmount">
        <template slot-scope="scope">
          {{ scope.row.tokenAmount }} XLS
        </template>
      </el-table-column>

<!--      <el-table-column label="支付币种" align="center" prop="payToken" />-->
      <el-table-column align="center" label="支付金额" prop="payAmount" >
        <template slot-scope="scope">
          {{ scope.row.payAmount }} OKB
        </template>
      </el-table-column>
      <el-table-column align="center" label="链上交易哈希" prop="txHash" />
<!--      <el-table-column label="锁定时间" align="center" prop="lockedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lockedAt) }}</span>
        </template>
      </el-table-column>-->


      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="业务状态是否处理" prop="bizStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.bizStatus"/>
        </template>
      </el-table-column>


      <el-table-column align="center" label="超时时间" prop="lockExpireAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lockExpireAt) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="领取时间" prop="completedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completedAt) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="超时/取消/失败原因" align="center" prop="releaseReason" />-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->

<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:airdropClaim:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:airdropClaim:remove']"
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

    <!-- 添加或修改空投领取记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="领取流水号/锁定号" prop="claimNo">
          <el-input v-model="form.claimNo" placeholder="请输入领取流水号/锁定号" />
        </el-form-item>
        <el-form-item label="轮次编号" prop="roundNo">
          <el-input v-model="form.roundNo" placeholder="请输入轮次编号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="0锁定 1支付中 2成功 3超时 4取消 5失败" prop="status">
          <el-select v-model="form.status" placeholder="请选择0锁定 1支付中 2成功 3超时 4取消 5失败">
            <el-option
              v-for="dict in dict.type.t_airdrop_claim_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="锁定超时时间，默认锁定时间+5分钟" prop="lockExpireAt">
          <el-date-picker v-model="form.lockExpireAt"
            clearable
            placeholder="请选择锁定超时时间，默认锁定时间+5分钟"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="本次应发放的代币数量" prop="tokenAmount">
          <el-input v-model="form.tokenAmount" placeholder="请输入本次应发放的代币数量" />
        </el-form-item>
        <el-form-item label="链上交易哈希" prop="txHash">
          <el-input v-model="form.txHash" placeholder="请输入链上交易哈希" />
        </el-form-item>
        <el-form-item label="支付币种" prop="payToken">
          <el-input v-model="form.payToken" placeholder="请输入支付币种" />
        </el-form-item>
        <el-form-item label="支付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入支付金额" />
        </el-form-item>
        <el-form-item label="锁定时间" prop="lockedAt">
          <el-date-picker v-model="form.lockedAt"
            clearable
            placeholder="请选择锁定时间"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="终态时间" prop="completedAt">
          <el-date-picker v-model="form.completedAt"
            clearable
            placeholder="请选择终态时间"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="超时/取消/失败原因" prop="releaseReason">
          <el-input v-model="form.releaseReason" placeholder="请输入超时/取消/失败原因" />
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
import { listAirdropClaim, getAirdropClaim, delAirdropClaim, addAirdropClaim, updateAirdropClaim } from "@/api/xms/airdropClaim";

export default {
  name: "AirdropClaim",
  dicts: ['t_airdrop_claim_status','t_user_info_is_valid'],
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
      // 空投领取记录表格数据
      airdropClaimList: [],
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
        claimNo: null,
        roundNo: null,
        userId: null,
        status: null,
        lockExpireAt: null,
        tokenAmount: null,
        txHash: null,
        payToken: null,
        payAmount: null,
        lockedAt: null,
        completedAt: null,
        releaseReason: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        claimNo: [
          { required: true, message: "领取流水号/锁定号不能为空", trigger: "blur" }
        ],
        roundNo: [
          { required: true, message: "轮次编号不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "0锁定 1支付中 2成功 3超时 4取消 5失败不能为空", trigger: "change" }
        ],
        lockExpireAt: [
          { required: true, message: "锁定超时时间，默认锁定时间+5分钟不能为空", trigger: "blur" }
        ],
        tokenAmount: [
          { required: true, message: "本次应发放的代币数量不能为空", trigger: "blur" }
        ],
        lockedAt: [
          { required: true, message: "锁定时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询空投领取记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listAirdropClaim(this.queryParams).then(response => {
        this.airdropClaimList = response.rows;
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
        claimNo: null,
        roundNo: null,
        userId: null,
        status: null,
        lockExpireAt: null,
        tokenAmount: null,
        txHash: null,
        payToken: null,
        payAmount: null,
        lockedAt: null,
        completedAt: null,
        releaseReason: null,
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
      this.title = "添加空投领取记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAirdropClaim(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改空投领取记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAirdropClaim(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAirdropClaim(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除空投领取记录编号为"' + ids + '"的数据项？').then(function() {
        return delAirdropClaim(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/airdropClaim/export', {
        ...this.queryParams
      }, `airdropClaim_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
