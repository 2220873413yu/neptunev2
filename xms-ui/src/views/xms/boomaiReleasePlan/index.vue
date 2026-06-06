<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="钱包地址" prop="userAccount">
        <el-input
          v-model="queryParams.userAccount"
          clearable
          placeholder="请输入钱包地址"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="来源类型" label-width="120px" prop="sourceType">
        <el-select v-model="queryParams.sourceType" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_boomai_release_plan_source_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" label-width="120px" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_boomai_release_plan_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!--      <el-form-item label="产出日期" prop="produceDate">
              <el-input
                v-model="queryParams.produceDate"
                placeholder="请输入产出日期"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="关联订单号/业务单号，可选" prop="sourceOrderNo">
              <el-input
                v-model="queryParams.sourceOrderNo"
                placeholder="请输入关联订单号/业务单号，可选"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>-->
<!--      <el-form-item label="本条计划总共要释放的boomai数量，例如12" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          placeholder="请输入本条计划总共要释放的boomai数量，例如12"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已经释放的数量" prop="releasedAmount">
        <el-input
          v-model="queryParams.releasedAmount"
          placeholder="请输入已经释放的数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总释放天数：120/90/60/30/10" prop="totalDays">
        <el-input
          v-model="queryParams.totalDays"
          placeholder="请输入总释放天数：120/90/60/30/10"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="剩余释放天数" prop="unreleasedDays">
        <el-input
          v-model="queryParams.unreleasedDays"
          placeholder="请输入剩余释放天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始释放日期" prop="startDate">
        <el-date-picker clearable
          v-model="queryParams.startDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始释放日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="计划结束日期" prop="endDate">
        <el-date-picker clearable
          v-model="queryParams.endDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择计划结束日期">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item label="创建时间">
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
          v-hasPermi="['xms:boomaiReleasePlan:add']"
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
          v-hasPermi="['xms:boomaiReleasePlan:edit']"
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
          v-hasPermi="['xms:boomaiReleasePlan:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:boomaiReleasePlan:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="boomaiReleasePlanList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="订单号" prop="orderNo" />
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="钱包地址" prop="userAccount" />
<!--      <el-table-column label="产出日期 yyyymmdd，例如结算日" align="center" prop="produceDate" />-->
      <el-table-column align="center" label="来源类型" prop="sourceType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_boomai_release_plan_source_type" :value="scope.row.sourceType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="来源数据" prop="remark" width="160">
        <template slot-scope="scope">
          <span v-if="!scope.row.remark">--</span>
          <el-popover
            v-else
            placement="left"
            popper-class="source-popover"
            trigger="click"
            width="520"
            @hide="resetSourceTree(scope.row)"
            @show="loadSourceDetails(scope.row, 1)"
          >
            <div v-if="sourceLoadingMap[scope.row.id]" class="source-loading">加载中...</div>
            <div
              v-else-if="sourceDetailMap[scope.row.id] && sourceDetailMap[scope.row.id].length"
              class="source-list"
            >
              <div v-for="item in sourceDetailMap[scope.row.id]" :key="item.id" class="source-item">
                <div>订单号：{{ item.orderNo || '--' }}</div>
                <div>来源类型：{{ getSourceTypeLabel(item.sourceType) }}</div>
                <div>总产出：{{ item.totalAmount }} BOOMAI</div>
                <div>已产出：{{ item.releasedAmount }} BOOMAI</div>
                <div>计划产出周期：{{ item.totalDays }} 天</div>
                <div>剩余产出天数：{{ item.unreleasedDays }} 天</div>
                <div v-if="item.remark && maxSourceDepth >= 2" class="source-actions">
                  <el-button size="mini" type="text" @click="loadNestedSource(item, 2)">查看来源</el-button>
                </div>
                <div v-if="sourceLoadingMap[item.id]" class="source-loading nested">加载中...</div>
                <div
                  v-if="sourceDetailMap[item.id] && sourceDetailMap[item.id].length"
                  class="source-sublist"
                >
                  <div v-for="sub1 in sourceDetailMap[item.id]" :key="sub1.id" class="source-item nested">
                    <div>订单号：{{ sub1.orderNo || '--' }}</div>
                    <div>来源类型：{{ getSourceTypeLabel(sub1.sourceType) }}</div>
                    <div>总产出：{{ sub1.totalAmount }} BOOMAI</div>
                    <div>已产出：{{ sub1.releasedAmount }} BOOMAI</div>
                    <div>计划产出周期：{{ sub1.totalDays }} 天</div>
                    <div>剩余产出天数：{{ sub1.unreleasedDays }} 天</div>
                    <div v-if="sub1.remark && maxSourceDepth >= 3" class="source-actions">
                      <el-button size="mini" type="text" @click="loadNestedSource(sub1, 3)">查看来源</el-button>
                    </div>
                    <div v-if="sourceLoadingMap[sub1.id]" class="source-loading nested">加载中...</div>
                    <div
                      v-if="sourceDetailMap[sub1.id] && sourceDetailMap[sub1.id].length"
                      class="source-sublist"
                    >
                      <div v-for="sub2 in sourceDetailMap[sub1.id]" :key="sub2.id" class="source-item nested level3">
                        <div>订单号：{{ sub2.orderNo || '--' }}</div>
                        <div>来源类型：{{ getSourceTypeLabel(sub2.sourceType) }}</div>
                        <div>总产出：{{ sub2.totalAmount }} BOOMAI</div>
                        <div>已产出：{{ sub2.releasedAmount }} BOOMAI</div>
                        <div>计划产出周期：{{ sub2.totalDays }} 天</div>
                        <div>剩余产出天数：{{ sub2.unreleasedDays }} 天</div>
                        <div v-if="sub2.remark && maxSourceDepth >= 4" class="source-actions">
                          <el-button size="mini" type="text" @click="loadNestedSource(sub2, 4)">查看来源</el-button>
                        </div>
                        <div v-if="sourceLoadingMap[sub2.id]" class="source-loading nested">加载中...</div>
                        <div
                          v-if="sourceDetailMap[sub2.id] && sourceDetailMap[sub2.id].length"
                          class="source-sublist"
                        >
                          <div v-for="sub3 in sourceDetailMap[sub2.id]" :key="sub3.id" class="source-item nested level4">
                            <div>订单号：{{ sub3.orderNo || '--' }}</div>
                            <div>来源类型：{{ getSourceTypeLabel(sub3.sourceType) }}</div>
                            <div>总产出：{{ sub3.totalAmount }} BOOMAI</div>
                            <div>已产出：{{ sub3.releasedAmount }} BOOMAI</div>
                            <div>计划产出周期：{{ sub3.totalDays }} 天</div>
                            <div>剩余产出天数：{{ sub3.unreleasedDays }} 天</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="source-empty">未查询到来源数据</div>
            <el-button slot="reference" size="mini" type="text">查看来源</el-button>
          </el-popover>
        </template>
      </el-table-column>
<!--      <el-table-column label="关联订单号/业务单号，可选" align="center" prop="sourceOrderNo" />-->
     <el-table-column align="center" label="本次计划总产出(BOOMAI)" prop="totalAmount" />
      <el-table-column align="center" label="当前已产出(BOOMAI)" prop="releasedAmount" />
      <el-table-column align="center" label="计划产出周期(天)" prop="totalDays" />
      <el-table-column align="center" label="剩余产出天数" prop="unreleasedDays" />
<!--      <el-table-column label="开始释放日期" align="center" prop="startDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划结束日期" align="center" prop="endDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column align="center" label="状态" prop="status" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_boomai_release_plan_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createDate" />
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
            v-hasPermi="['xms:boomaiReleasePlan:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:boomaiReleasePlan:remove']"
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

    <!-- 添加或修改boomai收益线性释放计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="产出日期 yyyymmdd，例如结算日" prop="produceDate">
          <el-input v-model="form.produceDate" placeholder="请输入产出日期 yyyymmdd，例如结算日" />
        </el-form-item>
        <el-form-item label="关联订单号/业务单号，可选" prop="sourceOrderNo">
          <el-input v-model="form.sourceOrderNo" placeholder="请输入关联订单号/业务单号，可选" />
        </el-form-item>
        <el-form-item label="本条计划总共要释放的boomai数量，例如12" prop="totalAmount">
          <el-input v-model="form.totalAmount" placeholder="请输入本条计划总共要释放的boomai数量，例如12" />
        </el-form-item>
        <el-form-item label="已经释放的数量" prop="releasedAmount">
          <el-input v-model="form.releasedAmount" placeholder="请输入已经释放的数量" />
        </el-form-item>
        <el-form-item label="总释放天数：120/90/60/30/10" prop="totalDays">
          <el-input v-model="form.totalDays" placeholder="请输入总释放天数：120/90/60/30/10" />
        </el-form-item>
        <el-form-item label="剩余释放天数" prop="unreleasedDays">
          <el-input v-model="form.unreleasedDays" placeholder="请输入剩余释放天数" />
        </el-form-item>
        <el-form-item label="开始释放日期" prop="startDate">
          <el-date-picker v-model="form.startDate"
            clearable
            placeholder="请选择开始释放日期"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="计划结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate"
            clearable
            placeholder="请选择计划结束日期"
            type="date"
            value-format="yyyy-MM-dd">
          </el-date-picker>
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
import { listBoomaiReleasePlan, getBoomaiReleasePlan, delBoomaiReleasePlan, addBoomaiReleasePlan, updateBoomaiReleasePlan, getBoomaiReleasePlanSource } from "@/api/xms/boomaiReleasePlan";

export default {
  name: "BoomaiReleasePlan",
  dicts: ['t_boomai_release_plan_source_type', 't_boomai_release_plan_status'],
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
      // boomai收益线性释放计划表格数据
      boomaiReleasePlanList: [],
      // 记录 remark->来源列表
      sourceDetailMap: {},
      sourceLoadingMap: {},
      maxSourceDepth: 4,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 状态 0:进行中,1:已释放完,2:已被合并作废时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userAccount: null,
        userId: null,
        produceDate: null,
        sourceType: null,
        sourceOrderNo: null,
        totalAmount: null,
        releasedAmount: null,
        totalDays: null,
        unreleasedDays: null,
        startDate: null,
        endDate: null,
        status: null,
        createTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
        produceDate: [
          { required: true, message: "产出日期 yyyymmdd，例如结算日不能为空", trigger: "blur" }
        ],
        sourceType: [
          { required: true, message: "来源类型 0:每日结算,1:加速合并生成不能为空", trigger: "change" }
        ],
        totalAmount: [
          { required: true, message: "本条计划总共要释放的boomai数量，例如12不能为空", trigger: "blur" }
        ],
        releasedAmount: [
          { required: true, message: "已经释放的数量不能为空", trigger: "blur" }
        ],
        totalDays: [
          { required: true, message: "总释放天数：120/90/60/30/10不能为空", trigger: "blur" }
        ],
        unreleasedDays: [
          { required: true, message: "剩余释放天数不能为空", trigger: "blur" }
        ],
        startDate: [
          { required: true, message: "开始释放日期不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "计划结束日期不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态 0:进行中,1:已释放完,2:已被合并作废不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询boomai收益线性释放计划列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listBoomaiReleasePlan(this.queryParams).then(response => {
        this.boomaiReleasePlanList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.sourceDetailMap = {};
        this.sourceLoadingMap = {};
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
        userId: null,
        produceDate: null,
        sourceType: null,
        sourceOrderNo: null,
        totalAmount: null,
        releasedAmount: null,
        totalDays: null,
        unreleasedDays: null,
        startDate: null,
        endDate: null,
        status: null,
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
      this.title = "添加boomai收益线性释放计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBoomaiReleasePlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改boomai收益线性释放计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBoomaiReleasePlan(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBoomaiReleasePlan(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除boomai收益线性释放计划编号为"' + ids + '"的数据项？').then(function() {
        return delBoomaiReleasePlan(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/boomaiReleasePlan/export', {
        ...this.queryParams
      }, `boomaiReleasePlan_${new Date().getTime()}.xlsx`)
    },
    getSourceTypeLabel(type) {
      const dictList = this.dict.type.t_boomai_release_plan_source_type || [];
      const hit = dictList.find(item => item.value === String(type));
      return hit ? hit.label : (type ?? '--');
    },
    loadSourceDetails(row, depth = 1) {
      if (!row.remark || depth > this.maxSourceDepth) {
        return;
      }
      if (this.sourceDetailMap[row.id] || this.sourceLoadingMap[row.id]) {
        return;
      }
      this.$set(this.sourceLoadingMap, row.id, true);
      getBoomaiReleasePlanSource(row.remark)
        .then(response => {
          const list = response.data || [];
          this.$set(this.sourceDetailMap, row.id, list);
        })
        .finally(() => {
          this.$set(this.sourceLoadingMap, row.id, false);
        });
    },
    loadNestedSource(row, depth) {
      this.loadSourceDetails(row, depth);
    },
    resetSourceTree(row) {
      if (!row || !row.id) {
        return;
      }
      const visited = new Set();
      const clearBranch = (id) => {
        if (!id || visited.has(id)) {
          return;
        }
        visited.add(id);
        const children = this.sourceDetailMap[id];
        if (Array.isArray(children)) {
          children.forEach(child => clearBranch(child.id));
        }
        if (this.sourceDetailMap[id] !== undefined) {
          this.$delete(this.sourceDetailMap, id);
        }
        if (this.sourceLoadingMap[id] !== undefined) {
          this.$delete(this.sourceLoadingMap, id);
        }
      };
      clearBranch(row.id);
    }
  }
};
</script>

<style lang="scss" scoped>
.source-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 320px;
  overflow-y: auto;
  width: 100%;
}

.source-item {
  padding: 8px 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  font-size: 13px;
  line-height: 20px;
  background: #f9fafc;
}

.source-item.nested {
  background: #fff;
  border-style: dashed;
}

.source-sublist {
  margin-top: 8px;
  margin-left: 12px;
  border-left: 2px solid #e4e7ed;
  padding-left: 10px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.source-actions {
  margin-top: 4px;
}

.source-loading,
.source-empty {
  font-size: 13px;
  color: #909399;
}

::v-deep .source-popover {
  max-width: 620px !important;
  width: 520px;
}
</style>
