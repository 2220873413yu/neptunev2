<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="价格/U" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入价格/U"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="算力" prop="computingPower">
        <el-input
          v-model="queryParams.computingPower"
          placeholder="请输入算力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金类型 0:活期,1:定期,2:体验式基金" prop="cardType">
        <el-select v-model="queryParams.cardType" placeholder="请选择基金类型 0:活期,1:定期,2:体验式基金" clearable>
          <el-option
            v-for="dict in dict.type.card_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否上架 0:否,1:是" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择是否上架 0:否,1:是" clearable>
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序"
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
      <!--  <el-col :span="1.5">
      <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xms:cardPackage:add']"
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
          v-hasPermi="['xms:cardPackage:edit']"
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
          v-hasPermi="['xms:cardPackage:remove']"
        >删除</el-button>
      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:cardPackage:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardPackageList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>

      <el-table-column align="center" label="卡片等级" prop="cardType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_type" :value="scope.row.cardType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="价格/U" prop="price" >
        <template slot-scope="scope">
          <span>{{ scope.row.price }} U</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="卡片图片" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :height="50" :src="scope.row.image" :width="50"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="算力" prop="computingPower" >
      <template slot-scope="scope">
        <span>{{ scope.row.computingPower }} T</span>
      </template>
      </el-table-column>
      <el-table-column align="center" label="赠送BDAI比例" prop="validNum3GiftRatio" >
      <template slot-scope="scope">
        <span>{{ scope.row.validNum3GiftRatio }} %</span>
      </template>
      </el-table-column>
<!--      <el-table-column label="销量" align="center" prop="sales" />-->
      <el-table-column align="center" label="是否上架" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="排序" prop="sort" />
      <el-table-column align="center" label="remark" prop="remark" />
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:cardPackage:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:cardPackage:remove']"
          >删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改卡片套餐对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="卡片等级" prop="cardType">
          <el-select v-model="form.cardType" placeholder="请选择">
            <el-option
              v-for="dict in dict.type.card_type"
              :key="dict.value"
              :disabled="true"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input
            v-model="form.price"
            placeholder="请输入价格"
            @input="handlePriceInput"
          />
        </el-form-item>
        <el-form-item label="卡片图片" prop="image">
          <image-upload v-model="form.image" limit="1"/>
        </el-form-item>
        <el-form-item label="算力" prop="computingPower">
          <el-input v-model="form.computingPower"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入算力" />
        </el-form-item>

        <el-form-item label="赠送BDAI比例" prop="validNum3GiftRatio">
          <el-input v-model="form.validNum3GiftRatio"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入比例" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>

        <el-form-item label="是否上架" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入排序"
          />
        </el-form-item>
<!--        <el-form-item label="remark" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入remark" />
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
import { listCardPackage, getCardPackage, delCardPackage, addCardPackage, updateCardPackage } from "@/api/xms/cardPackage";
import { sanitizeNumberInput } from "@/utils/numberInput";

export default {
  name: "CardPackage",
  dicts: ['card_type', 't_user_info_is_valid'],
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
      // 卡片套餐表格数据
      cardPackageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        price: null,
        image: null,
        computingPower: null,
        cardType: null,
        sales: null,
        status: null,
        sort: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        price: [
          { required: true, message: "价格/U不能为空", trigger: "blur" }
        ],
        sort: [
          { required: true, message: "排序不能为空", trigger: "change" }
        ],
        computingPower: [
          { required: true, message: "算力不能为空", trigger: "change" }
        ],
        image: [
          { required: true, message: "图片不能为空", trigger: "blur" }
        ],
        validNum3GiftRatio: [
          { required: true, message: "赠送BDAI比例不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "是否上架 0:否,1:是不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 价格输入限制：仅数字且保留两位小数（可复用工具方法） */
    handlePriceInput(val) {
      this.form.price = sanitizeNumberInput(val, { allowDecimal: true, decimals: 2 });
    },
    /** 查询卡片套餐列表 */
    getList() {
      this.loading = true;
      listCardPackage(this.queryParams).then(response => {
        this.cardPackageList = response.rows;
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
        price: null,
        image: null,
        computingPower: null,
        validNum3GiftRatio: null,
        cardType: null,
        sales: null,
        status: null,
        createTime: null,
        updateTime: null,
        sort: null,
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
      this.title = "添加卡片套餐";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCardPackage(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡片套餐";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCardPackage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCardPackage(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除卡片套餐编号为"' + ids + '"的数据项？').then(function() {
        return delCardPackage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/cardPackage/export', {
        ...this.queryParams
      }, `cardPackage_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
