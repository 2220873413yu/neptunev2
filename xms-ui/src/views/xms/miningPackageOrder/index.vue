<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="矿机套餐id" prop="miningPackageId">
        <el-input
          v-model="queryParams.miningPackageId"
          placeholder="请输入矿机套餐id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="订单号" label-width="120px" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          clearable
          placeholder="请输入订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

<!--      <el-form-item label="用户UID" prop="userId" label-width="120px">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户UID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <el-form-item label="用户账号" label-width="120px" prop="userAccount">
        <el-input
          v-model="queryParams.userAccount"
          clearable
          placeholder="请输入用户账号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>


      <el-form-item label="订单天数" label-width="120px" prop="type">
        <el-select v-model="queryParams.days" placeholder="请选择天数">
          <el-option
            v-for="days in distinctDays"
            :key="days"
            :label="days"
            :value="days"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="购买金额(本金)" prop="buyPrice">
        <el-input
          v-model="queryParams.buyPrice"
          placeholder="请输入购买金额(本金)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
<!--      <el-form-item label="基金天数" prop="days">
        <el-input
          v-model="queryParams.days"
          placeholder="请输入基金天数"
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
      <el-form-item label="运行天数" prop="runDays">
        <el-input
          v-model="queryParams.runDays"
          placeholder="请输入运行天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="基金类型" label-width="120px" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择基金类型">
          <el-option
            v-for="dict in dict.type.t_w3_mining_package_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="来源类型" label-width="120px" prop="sourceType">
        <el-select v-model="queryParams.sourceType" clearable placeholder="请选择矿机来源类型">
          <el-option
            v-for="dict in dict.type.t_mining_package_order_source_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="日利率" prop="dayRatio">
        <el-input
          v-model="queryParams.dayRatio"
          placeholder="请输入日利率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="今日收益" prop="dayReward">
        <el-input
          v-model="queryParams.dayReward"
          placeholder="请输入今日收益"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="累计收益" prop="totalReward">
        <el-input
          v-model="queryParams.totalReward"
          placeholder="请输入累计收益"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->

      <el-form-item label="基金状态" label-width="120px" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择状态">
          <el-option
            v-for="dict in dict.type.t_mining_package_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="每日违约金递减率(如0.5%)" prop="dailyPenaltyReduction">
        <el-input
          v-model="queryParams.dailyPenaltyReduction"
          placeholder="请输入每日违约金递减率(如0.5%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="违约金比例(如20%)" prop="penaltyRate">
        <el-input
          v-model="queryParams.penaltyRate"
          placeholder="请输入违约金比例(如20%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="是否退本" label-width="120px"  prop="principalReturned">
        <el-select v-model="queryParams.principalReturned" clearable placeholder="请选择是否退本">
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
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:miningPackageOrder:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >基金拨付</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
                      @click="openChatDialog"
          v-hasPermi="['xms:miningPackageOrder:edit']"
        >聊天机器人对话</el-button>
      </el-col>-->
      <!--
      <el-col :span="1.5">
        <el-button
          type="danger"image.png
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xms:miningPackageOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:miningPackageOrder:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="miningPackageOrderList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column v-if="false" align="center" label="矿机套餐id" prop="miningPackageId"/>
      <el-table-column align="center" label="订单号" prop="orderNo" />
      <el-table-column align="center" label="用户UID" prop="userId" />
      <el-table-column align="center" label="用户账号" prop="userAccount" />
      <el-table-column align="center" label="本金" prop="buyPrice" />
<!--      <el-table-column label="是否退本" align="center" prop="principalReturned">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.principalReturned"/>
        </template>
      </el-table-column>-->

      <el-table-column align="center" label="退本状态" prop="returnedBizStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_mining_package_order_returned_biz_status" :value="scope.row.returnedBizStatus"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="基金天数" prop="days" />
      <el-table-column align="center" label="剩余天数" prop="haveDays" />
      <el-table-column align="center" label="释放天数(开放式基金)" prop="runDays" />
      <el-table-column align="center" label="当前运行天数(开放式基金)" prop="currentRunDays" />
      <el-table-column align="center" class-name="full-width-dict-tag" label="基金类型"   prop="type" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_w3_mining_package_type"
          :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="来源类型" prop="sourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_mining_package_order_source_type" :value="scope.row.sourceType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="日利率" prop="dayRatio" >
      <template slot-scope="scope">
        {{scope.row.dayRatio}}%
      </template>
      </el-table-column>
<!--      <el-table-column label="今日收益" align="center" prop="dayReward" />-->
      <el-table-column align="center" label="累计收益" prop="totalReward" />
      <el-table-column align="center" label="待释放" prop="pendingReward" />

<!--      <el-table-column label="基金套餐的快照信息" align="center" prop="remark" />-->
      <el-table-column align="center" label="基金状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_mining_package_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="业务状态 0:代表订单没有结算团队业绩后续业务,1:代表订单已经结算团队业绩后续业务" align="center" prop="bizStatus" />-->
      <el-table-column align="center" label="每日违约金递减率" prop="dailyPenaltyReduction">
        <template slot-scope="scope">
          {{scope.row.dailyPenaltyReduction}}%
        </template>
      </el-table-column>
      <el-table-column align="center" label="违约金比例" prop="penaltyRate" >
      <template slot-scope="scope">
        {{scope.row.penaltyRate}}%
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
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:miningPackageOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:miningPackageOrder:remove']"
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

    <!-- 添加或修改基金订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
<!--        <el-form-item label="矿机套餐id" prop="miningPackageId">
          <el-input v-model="form.miningPackageId" placeholder="请输入矿机套餐id" />
        </el-form-item>-->

        <el-form-item label="基金套餐" prop="miningPackageId">
          <!-- 调试信息：显示数据数量 -->
<!--          <div style="font-size: 12px; color: #999; margin-bottom: 5px;">
            套餐数量: {{ funcDTOS ? funcDTOS.length : 0 }}
            | 数据类型: {{ typeof funcDTOS }}
            | 是否数组: {{ Array.isArray(funcDTOS) }}
            <el-button size="mini" type="text" @click="loadMiningPackageList" style="margin-left: 10px;">
              🔄 重新加载
            </el-button>
            <el-button size="mini" type="text" @click="debugFuncDTOS" style="margin-left: 5px;">
              🔍 调试
            </el-button>
          </div>-->
          <!-- 显示原始数据供调试 -->
<!--          <div v-if="funcDTOS && funcDTOS.length > 0" style="font-size: 10px; color: #666; margin-bottom: 5px; max-height: 60px; overflow-y: auto;">
            <div v-for="(item, index) in funcDTOS" :key="index">
              {{ index + 1 }}. ID:{{ item.id }} - {{ item.nameCn }}
            </div>
          </div>-->
          <el-select v-model="form.miningPackageId" clearable filterable placeholder="请选择套餐">
            <el-option
              v-for="item in funcDTOS"
              :key="item.id"
              :label="item.nameCn"
              :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="用户账号" prop="userAccount">
          <el-input v-model="form.userAccount"  maxlength="20" show-word-limitplaceholder="请输入用户账号" />
        </el-form-item>

        <el-form-item label="投入金额" prop="buyPrice">
          <el-input v-model="form.buyPrice"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入投入金额" />
        </el-form-item>

        <el-form-item label="领取1U所需新增业绩" prop="pointsPerUsdt">
          <el-input v-model="form.pointsPerUsdt"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入领取1U新增业绩" />
        </el-form-item>
        <el-form-item  :rules="[{ required: true, message: '请输入google验证码' }]" label="google验证码" prop="autoCode">
          <el-input v-model="form.autoCode" placeholder="google验证码"></el-input>
        </el-form-item>
<!--        <el-form-item label="用户UID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户UID" />
        </el-form-item>
        <el-form-item label="购买金额(本金)" prop="buyPrice">
          <el-input v-model="form.buyPrice" placeholder="请输入购买金额(本金)" />
        </el-form-item>
        <el-form-item label="基金天数" prop="days">
          <el-input v-model="form.days" placeholder="请输入基金天数" />
        </el-form-item>
        <el-form-item label="剩余天数" prop="haveDays">
          <el-input v-model="form.haveDays" placeholder="请输入剩余天数" />
        </el-form-item>
        <el-form-item label="运行天数" prop="runDays">
          <el-input v-model="form.runDays" placeholder="请输入运行天数" />
        </el-form-item>
        <el-form-item label="基金类型 0:活期,1:固定" prop="type">
          <el-select v-model="form.type" placeholder="请选择基金类型 0:活期,1:固定">
            <el-option
              v-for="dict in dict.type.t_w3_mining_package_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日利率" prop="dayRatio">
          <el-input v-model="form.dayRatio" placeholder="请输入日利率" />
        </el-form-item>
        <el-form-item label="今日收益" prop="dayReward">
          <el-input v-model="form.dayReward" placeholder="请输入今日收益" />
        </el-form-item>
        <el-form-item label="累计收益" prop="totalReward">
          <el-input v-model="form.totalReward" placeholder="请输入累计收益" />
        </el-form-item>
        <el-form-item label="基金套餐的快照信息" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="状态 0:释放中,1:已经达到最大倍数,2:已结束" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态 0:释放中,1:已经达到最大倍数,2:已结束">
            <el-option
              v-for="dict in dict.type.t_mining_package_order_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="每日违约金递减率(如0.5%)" prop="dailyPenaltyReduction">
          <el-input v-model="form.dailyPenaltyReduction" placeholder="请输入每日违约金递减率(如0.5%)" />
        </el-form-item>
        <el-form-item label="违约金比例(如20%)" prop="penaltyRate">
          <el-input v-model="form.penaltyRate" placeholder="请输入违约金比例(如20%)" />
        </el-form-item>
        <el-form-item label="是否退本 0:否,1:是" prop="principalReturned">
          <el-select v-model="form.principalReturned" placeholder="请选择是否退本 0:否,1:是">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 聊天机器人对话框 -->
    <el-dialog :visible.sync="chatDialogVisible" append-to-body title="AI智能助手" width="900px">
      <div class="chat-container">
        <!-- 聊天记录区域 -->
        <div ref="chatMessages" class="chat-messages">
          <div v-for="(message, index) in chatMessages" :key="index" class="message-item">
            <!-- 用户消息 -->
            <div v-if="message.type === 'user'" class="user-message">
              <div class="message-content user-content">
                {{ message.content }}
              </div>
              <div class="message-time">{{ message.time }}</div>
            </div>
            <!-- AI回复 -->
            <div v-else class="ai-message">
              <div class="ai-avatar">🤖</div>
              <div class="message-content ai-content">
                <!-- 流式接收中显示原始文本，完成后显示 Markdown 渲染 -->
                <div v-if="message.isStreaming" style="white-space: pre-wrap;">{{ message.content }}</div>
                <div v-else v-html="formatMessage(message.content)"></div>
              </div>
              <div class="message-time">{{ message.time }}</div>
            </div>
          </div>
          <!-- 加载中提示 -->
          <div v-if="chatLoading" class="loading-message">
            <div class="ai-avatar">🤖</div>
            <div class="message-content ai-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
              正在思考中...
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-area">
          <el-input
            v-model="currentMessage"
            :disabled="chatLoading"
            :rows="3"
            placeholder="请输入您的问题..."
            type="textarea"
            @keydown.enter.ctrl="sendMessage"
          />
          <div class="input-actions">
            <el-button
              :disabled="!currentMessage.trim()"
              :loading="chatLoading"
              size="small"
              type="primary"
              @click="sendMessage"
            >
              发送 (Ctrl+Enter)
            </el-button>
            <el-button size="small" @click="clearChat">清空对话</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  doGetDistinctDays,
  listMiningPackageOrder,
  getMiningPackageOrder,
  delMiningPackageOrder,
  addMiningPackageOrder,
  updateMiningPackageOrder,
  listMiningPackageList
} from "@/api/xms/miningPackageOrder";
import request from '@/utils/request';

// 使用 CDN 方式加载库 - 在组件挂载后检查
// const marked = window.marked;
// const DOMPurify = window.DOMPurify;

// 流式聊天API函数
function chatWithAIStream(text, onMessage, onError, onComplete) {
  console.log('开始流式聊天请求:', text);
  return new Promise((resolve, reject) => {
    fetch('http://localhost:19965/bot/handleWord', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'text/event-stream',
        'Cache-Control': 'no-cache'
      },
      body: JSON.stringify({
        text: text
      })
    })
    .then(response => {
      console.log('收到响应状态:', response.status);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const reader = response.body.getReader();
      const decoder = new TextDecoder();
      let buffer = '';

      function pump() {
        console.log('开始pump读取流式数据');
        return reader.read().then(({ done, value }) => {
          console.log('pump读取结果 - done:', done, 'value:', value);
          if (done) {
            console.log('流式读取完成，剩余buffer:', buffer);
            // 处理剩余的缓冲区数据
            if (buffer.trim()) {
              const finalLine = buffer.trim();
              console.log('处理最后的数据:', finalLine);
              if (finalLine.startsWith('data: ')) {
                const content = finalLine.substring(6);
                if (content && content !== '[DONE]') {
                  console.log('发送最后的标准SSE内容:', content);
                  onMessage && onMessage(content);
                }
              } else if (finalLine.startsWith('data:')) {
                const content = finalLine.substring(5);
                if (content && content !== '[DONE]') {
                  console.log('发送最后的非标准SSE内容:', content);
                  onMessage && onMessage(content);
                }
              } else if (finalLine && !finalLine.startsWith('event:') && !finalLine.startsWith('id:') && !finalLine.startsWith(':')) {
                console.log('发送最后的其他内容:', finalLine);
                onMessage && onMessage(finalLine);
              }
            }
            onComplete && onComplete();
            resolve();
            return;
          }

          const chunk = decoder.decode(value, { stream: true });
          buffer += chunk;
          console.log('收到数据块:', chunk);
          console.log('当前buffer:', buffer);

          // 极简处理：直接处理所有完整行
          const lines = buffer.split('\n');

          // 处理除最后一行外的所有行
          for (let i = 0; i < lines.length - 1; i++) {
            const line = lines[i];
            console.log('原始行数据:', JSON.stringify(line));

            // 处理SSE格式的多种情况
            if (line.startsWith('data: ')) {
              // 标准SSE格式：data: 内容
              const content = line.substring(6);
              console.log('标准SSE格式解析出:', JSON.stringify(content));
              if (content && content !== '[DONE]') {
                onMessage && onMessage(content);
              }
            } else if (line.startsWith('data:')) {
              // 非标准SSE格式：data:内容（没有空格）
              const content = line.substring(5);
              console.log('非标准SSE格式解析出:', JSON.stringify(content));
              if (content && content !== '[DONE]') {
                onMessage && onMessage(content);
              }
            } else if (line.trim() && !line.startsWith('event:') && !line.startsWith('id:') && !line.startsWith(':')) {
              // 其他内容（排除SSE控制字符）
              console.log('其他内容解析出:', JSON.stringify(line.trim()));
              onMessage && onMessage(line.trim());
            }
          }

          // 保留最后一行
          buffer = lines[lines.length - 1] || '';
          console.log('保留到buffer:', JSON.stringify(buffer));

          return pump();
        });
      }

      return pump();
    })
    .catch(error => {
      console.error('流式请求失败:', error);
      onError && onError(error);
      reject(error);
    });
  });
}

export default {
  name: "MiningPackageOrder",
  dicts: ['t_w3_mining_package_type', 't_mining_package_order_source_type', 't_mining_package_order_status', 't_mining_package_order_returned_biz_status', 't_user_info_is_valid'],
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
      // 基金订单表格数据
      miningPackageOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否退本 0:否,1:是时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        miningPackageId: null,
        orderNo: null,
        userId: null,
        userAccount: null,
        buyPrice: null,
        sourceType: null,
        days: null,
        haveDays: null,
        runDays: null,
        type: null,
        dayRatio: null,
        dayReward: null,
        totalReward: null,
        createTime: null,
        status: null,
        bizStatus: null,
        dailyPenaltyReduction: null,
        penaltyRate: null,
        principalReturned: null
      },
      // 表单参数
      form: {},
      funcDTOS: [],
      distinctDays: [],
      // 聊天机器人相关数据
      chatDialogVisible: false,
      chatMessages: [],
      currentMessage: '',
      chatLoading: false,
      scrollTimeout: null,
      updateTimeout: null,
      // 表单校验
      rules: {
        miningPackageId: [
          { required: true, message: "请选择基金套餐", trigger: "change" }
        ],
        userAccount: [
          { required: true, message: "用户账号不能为空", trigger: "blur" }
        ],
        pointsPerUsdt: [
          { required: true, message: "所需积分不能为空", trigger: "blur" }
        ],
        buyPrice: [
          { required: true, message: "投入金额不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户UID不能为空", trigger: "blur" }
        ],
        days: [
          { required: true, message: "基金天数不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.loadMiningPackageList();
    this.getList();

    doGetDistinctDays().then(response => {
      this.distinctDays = response;
    });
    // 检查 Markdown 库是否加载
    this.$nextTick(() => {
      setTimeout(() => {
        console.log('检查 Markdown 库加载状态:');
        console.log('window.marked:', window.marked);
        console.log('window.DOMPurify:', window.DOMPurify);

        if (window.marked && window.DOMPurify) {
          console.log('✅ Markdown 库加载成功');
        } else {
          console.warn('❌ Markdown 库未正确加载');
        }
      }, 1000);
    });
  },
  methods: {
    /** 加载基金套餐列表 */
    loadMiningPackageList() {
      console.log('开始加载基金套餐列表...');
      console.log('当前 funcDTOS:', this.funcDTOS);

      listMiningPackageList().then(response => {

        // 尝试多种可能的数据路径
        let dataArray = null;
        if (response.data && Array.isArray(response.data)) {
          dataArray = response.data;
        } else if (response.rows && Array.isArray(response.rows)) {
          dataArray = response.rows;
        } else if (response && Array.isArray(response)) {
          dataArray = response;
        }

        if (dataArray && dataArray.length > 0) {
          console.log('找到数据数组，长度:', dataArray.length);
          console.log('第一个元素:', dataArray[0]);

          // 强制设置数据
          this.funcDTOS = dataArray;
          this.$forceUpdate(); // 强制更新视图

          console.log('设置后的 funcDTOS:', this.funcDTOS);
          console.log('设置后的长度:', this.funcDTOS.length);

          // 验证数据结构
          dataArray.forEach((item, index) => {
            console.log(`套餐 ${index + 1}:`, {
              id: item.id,
              nameCn: item.nameCn,
              type: typeof item.nameCn
            });
          });
        } else {
          console.warn('未找到有效的数据数组');
          console.warn('完整响应结构:', response);
          this.funcDTOS = [];
        }
      }).catch(error => {
        console.error('加载基金套餐列表失败:', error);
        this.$modal.msgError('加载基金套餐列表失败: ' + error.message);
        this.funcDTOS = [];
      });
    },
    /** 查询基金订单列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listMiningPackageOrder(this.queryParams).then(response => {
        this.miningPackageOrderList = response.rows;
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
        miningPackageId: null,
        orderNo: null,
        userId: null,
        userAccount: null,
        pointsPerUsdt: null,
        buyPrice: null,
        days: null,
        haveDays: null,
        runDays: null,
        type: null,
        dayRatio: null,
        dayReward: null,
        totalReward: null,
        createTime: null,
        autoCode: null,
        updateTime: null,
        remark: null,
        status: null,
        bizStatus: null,
        dailyPenaltyReduction: null,
        penaltyRate: null,
        principalReturned: null
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
      // 重新加载套餐列表，确保数据是最新的
      this.loadMiningPackageList();
      this.open = true;
      this.title = "添加基金订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // 重新加载套餐列表，确保数据是最新的
      this.loadMiningPackageList();
      const id = row.id || this.ids
      getMiningPackageOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改基金订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMiningPackageOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMiningPackageOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除基金订单编号为"' + ids + '"的数据项？').then(function() {
        return delMiningPackageOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/miningPackageOrder/export', {
        ...this.queryParams
      }, `miningPackageOrder_${new Date().getTime()}.xlsx`)
    },
    /** 调试funcDTOS数据 */
    debugFuncDTOS() {
      console.log('=== funcDTOS 调试信息 ===');
      console.log('funcDTOS:', this.funcDTOS);
      console.log('类型:', typeof this.funcDTOS);
      console.log('是否数组:', Array.isArray(this.funcDTOS));
      console.log('长度:', this.funcDTOS ? this.funcDTOS.length : 'undefined');
      console.log('原始数据:', JSON.stringify(this.funcDTOS, null, 2));

      if (this.funcDTOS && this.funcDTOS.length > 0) {
        console.log('第一个元素的详细信息:');
        const first = this.funcDTOS[0];
        console.log('- 完整对象:', first);
        console.log('- id:', first.id);
        console.log('- nameCn:', first.nameCn);
        console.log('- 所有属性:', Object.keys(first));
      }

      // 弹窗显示信息
      this.$alert(`
        funcDTOS 调试信息:
        - 类型: ${typeof this.funcDTOS}
        - 是否数组: ${Array.isArray(this.funcDTOS)}
        - 长度: ${this.funcDTOS ? this.funcDTOS.length : 'undefined'}
        - 详情请查看控制台
      `, '调试信息', {
        confirmButtonText: '确定'
      });
    },
    /** 打开聊天对话框 */
    openChatDialog() {
      this.chatDialogVisible = true;
      if (this.chatMessages.length === 0) {
        // 添加欢迎消息
        this.chatMessages.push({
          type: 'ai',
          content: `您好！我是您的**AI基金投资顾问助手**，可以为您提供专业的基金投资建议和市场分析。

## 🔍 我能帮您：
- **基金推荐**：个性化投资建议
- **市场分析**：实时行情解读
- **风险评估**：投资组合优化

> 💡 **温馨提示**：AI回复可能需要一些时间（最长60秒），请耐心等待。

请问有什么可以帮助您的吗？`,
          time: this.getCurrentTime(),
          isStreaming: false  // 欢迎消息已完成，直接渲染 Markdown
        });
      }
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
    /** 发送消息 */
    async sendMessage() {
      if (!this.currentMessage.trim() || this.chatLoading) {
        return;
      }

      const userMessage = this.currentMessage.trim();

      // 添加用户消息
      this.chatMessages.push({
        type: 'user',
        content: userMessage,
        time: this.getCurrentTime()
      });

      // 清空输入框
      this.currentMessage = '';

      // 滚动到底部
      this.$nextTick(() => {
        this.scrollToBottom();
      });

      // 开始加载
      this.chatLoading = true;

      // 用于存储AI消息的索引，初始为-1表示还未创建
      let aiMessageIndex = -1;

      try {
                // 调用流式聊天API
        await chatWithAIStream(
          userMessage,
          // onMessage: 接收到流式数据时的回调
          (chunk) => {
            console.log('【UI更新】收到流式数据:', chunk);

            // 如果AI消息还未创建，创建它
            if (aiMessageIndex === -1) {
              aiMessageIndex = this.chatMessages.length;
              console.log('【消息创建】首次接收数据，创建AI消息，索引:', aiMessageIndex);
              this.chatMessages.push({
                type: 'ai',
                content: '',
                time: this.getCurrentTime(),
                isStreaming: true  // 标记为流式接收中
              });
              console.log('【消息创建】创建后消息数组长度:', this.chatMessages.length);
            }

            console.log('【UI更新】当前消息内容:', this.chatMessages[aiMessageIndex].content);

            // 直接更新UI，不使用二级缓冲
            this.chatMessages[aiMessageIndex].content += chunk;
            console.log('📥 接收到chunk:', JSON.stringify(chunk));
            console.log('📝 当前累积内容长度:', this.chatMessages[aiMessageIndex].content.length);

            // 强制Vue响应式更新
            this.$forceUpdate();

            console.log('【UI更新】更新后消息内容:', this.chatMessages[aiMessageIndex].content);
            console.log('【UI更新】消息索引:', aiMessageIndex);
            console.log('【UI更新】消息数组长度:', this.chatMessages.length);

            // 滚动到底部（减少频率，提高性能）
            clearTimeout(this.scrollTimeout);
            this.scrollTimeout = setTimeout(() => {
              this.$nextTick(() => {
                this.scrollToBottom();
              });
            }, 100);
          },
          // onError: 发生错误时的回调
          (error) => {
            console.error('流式聊天请求失败:', error);

            let errorMessage = '抱歉，网络连接异常，请稍后重试。';

            // 根据错误类型提供不同的提示
            if (error.message && error.message.includes('timeout')) {
              errorMessage = '请求超时，AI正在深度思考中，请稍后重试。';
            } else if (error.message && error.message.includes('HTTP error')) {
              errorMessage = `服务器响应错误，请稍后重试。(${error.message})`;
            }

            // 如果AI消息还未创建，先创建它
            if (aiMessageIndex === -1) {
              aiMessageIndex = this.chatMessages.length;
              this.chatMessages.push({
                type: 'ai',
                content: errorMessage,
                time: this.getCurrentTime(),
                isStreaming: false  // 错误消息直接完成
              });
            } else {
              // 如果AI消息还是空的，设置错误消息
              if (!this.chatMessages[aiMessageIndex].content.trim()) {
                this.chatMessages[aiMessageIndex].content = errorMessage;
              } else {
                // 如果已经有部分内容，添加错误提示
                this.chatMessages[aiMessageIndex].content += '\n\n❌ 连接中断，回复可能不完整。';
              }
              // 标记流式接收完成（即使是错误）
              this.chatMessages[aiMessageIndex].isStreaming = false;
            }
          },
                    // onComplete: 流式响应完成时的回调
          () => {
            console.log('流式响应完成');

            // 如果AI消息还未创建（没有接收到任何数据），创建一个默认消息
            if (aiMessageIndex === -1) {
              aiMessageIndex = this.chatMessages.length;
              this.chatMessages.push({
                type: 'ai',
                content: '抱歉，我暂时无法回答您的问题。',
                time: this.getCurrentTime()
              });
            } else {
              console.log('AI消息内容:', this.chatMessages[aiMessageIndex].content);
              // 如果AI消息内容为空，设置默认消息
              if (!this.chatMessages[aiMessageIndex].content.trim()) {
                this.chatMessages[aiMessageIndex].content = '抱歉，我暂时无法回答您的问题。';
              }

              // 标记流式接收完成，触发 Markdown 渲染
              console.log('📝 流式接收完成，开始 Markdown 渲染');
              console.log('最终内容:', this.chatMessages[aiMessageIndex].content);
              this.chatMessages[aiMessageIndex].isStreaming = false;
              this.$forceUpdate(); // 强制更新以应用 Markdown 渲染

              // 验证 formatMessage 是否被调用
              setTimeout(() => {
                console.log('🎨 Markdown 渲染后的状态:', this.chatMessages[aiMessageIndex].isStreaming);
              }, 100);
            }

            // 停止加载状态
            this.chatLoading = false;

            // 滚动到底部
            this.$nextTick(() => {
              this.scrollToBottom();
            });
          }
        );

      } catch (error) {
        console.error('聊天请求失败:', error);

        // 如果AI消息还未创建，先创建它
        if (aiMessageIndex === -1) {
          aiMessageIndex = this.chatMessages.length;
          this.chatMessages.push({
            type: 'ai',
            content: '抱歉，网络连接异常，请稍后重试。',
            time: this.getCurrentTime(),
            isStreaming: false  // 错误消息直接完成
          });
        } else {
          // 如果AI消息还是空的，设置错误消息
          if (!this.chatMessages[aiMessageIndex].content.trim()) {
            this.chatMessages[aiMessageIndex].content = '抱歉，网络连接异常，请稍后重试。';
          }
          // 标记流式接收完成（即使是错误）
          this.chatMessages[aiMessageIndex].isStreaming = false;
        }
      } finally {
        this.chatLoading = false;

        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },
    /** 清空对话 */
    clearChat() {
      this.$confirm('确定要清空所有对话记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.chatMessages = [];
        this.openChatDialog(); // 重新添加欢迎消息
      });
    },
          /** 格式化消息内容 - 使用 Markdown 渲染 */
      formatMessage(content) {
        console.log('🎯 formatMessage 被调用');
        console.log('📄 输入内容长度:', content?.length);
        console.log('📝 输入内容预览:', content?.substring(0, 200) + '...');
        if (!content) return '';

        try {
          // 检查库是否加载
          if (!window.marked || !window.DOMPurify) {
            console.warn('Markdown 库未加载，使用基本格式化');
            console.log('window.marked:', window.marked);
            console.log('window.DOMPurify:', window.DOMPurify);
            return content.replace(/\n/g, '<br>');
          }

          // 配置 marked 选项（兼容旧版本）
          if (window.marked.setOptions) {
            window.marked.setOptions({
              breaks: true,        // 支持换行符转换为 <br>
              gfm: true,          // 启用 GitHub Flavored Markdown
              sanitize: false     // 不在 marked 中清理，交给 DOMPurify
            });
          }

          // 只进行最基本的格式修复，避免破坏正确的格式
          let processedContent = content;

          // 只修复明确有问题的格式：### 后面没有空格且不是在行首的情况
          // 例如：###货币基金 -> ### 货币基金
          processedContent = processedContent.replace(/^(#{1,6})([^\s#\n])/gm, '$1 $2');

          console.log('📝 预处理前:', content.substring(0, 200));
          console.log('🔧 预处理后:', processedContent.substring(0, 200));

          // 使用 marked 将 Markdown 转换为 HTML
          // 兼容不同版本的 marked API
          let rawHtml;
          if (typeof window.marked.parse === 'function') {
            rawHtml = window.marked.parse(processedContent);
          } else if (typeof window.marked === 'function') {
            rawHtml = window.marked(processedContent);
          } else {
            throw new Error('无法识别的 marked API');
          }
          console.log('marked 原始输出：', rawHtml);

          // 使用 DOMPurify 清理 HTML，防止 XSS 攻击
          const cleanHtml = window.DOMPurify.sanitize(rawHtml, {
            ALLOWED_TAGS: ['p', 'br', 'strong', 'em', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
                          'ul', 'ol', 'li', 'blockquote', 'code', 'pre', 'a', 'div', 'span'],
            ALLOWED_ATTR: ['href', 'target', 'rel', 'class']
          });

          console.log('DOMPurify 清理后：', cleanHtml);
          return cleanHtml;
        } catch (error) {
          console.error('Markdown 渲染失败:', error);
          // 如果渲染失败，返回原始内容并进行基本的换行处理
          return content.replace(/\n/g, '<br>');
        }
      },
    /** 获取当前时间 */
    getCurrentTime() {
      const now = new Date();
      return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;
    },
    /** 滚动到底部 */
    scrollToBottom() {
      if (this.$refs.chatMessages) {
        this.$refs.chatMessages.scrollTop = this.$refs.chatMessages.scrollHeight;
      }
    }
  }
};
</script>

<style scoped>
/* 聊天容器样式 */
.chat-container {
  height: 650px;
  display: flex;
  flex-direction: column;
}

/* 聊天记录区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 8px;
  margin-bottom: 15px;
}

/* 消息项样式 */
.message-item {
  margin-bottom: 15px;
}

/* 用户消息样式 */
.user-message {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.user-content {
  background-color: #409EFF;
  color: white;
  max-width: 70%;
  word-wrap: break-word;
}

/* AI消息样式 */
.ai-message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.ai-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #67C23A;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.ai-content {
  background-color: white;
  border: 1px solid #e4e7ed;
  max-width: 70%;
  word-wrap: break-word;
}

/* Markdown 渲染样式 */
.ai-content {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
  line-height: 1.6;
}

/* 标题样式 */
.ai-content h1, .ai-content h2, .ai-content h3, .ai-content h4, .ai-content h5, .ai-content h6 {
  margin: 16px 0 8px 0;
  font-weight: 600;
  line-height: 1.25;
  color: #24292e;
}

.ai-content h1 { font-size: 20px; border-bottom: 1px solid #eaecef; padding-bottom: 8px; }
.ai-content h2 { font-size: 18px; }
.ai-content h3 { font-size: 16px; }
.ai-content h4 { font-size: 14px; }
.ai-content h5 { font-size: 13px; }
.ai-content h6 { font-size: 12px; color: #6a737d; }

/* 段落样式 */
.ai-content p {
  margin: 8px 0;
  line-height: 1.6;
}

/* 强调样式 */
.ai-content strong {
  font-weight: 600;
  color: #24292e;
}

.ai-content em {
  font-style: italic;
  color: #6a737d;
}

/* 列表样式 */
.ai-content ul, .ai-content ol {
  margin: 8px 0;
  padding-left: 20px;
}

.ai-content ul {
  list-style-type: disc;
}

.ai-content ol {
  list-style-type: decimal;
}

.ai-content li {
  margin: 4px 0;
  line-height: 1.5;
}

.ai-content ul ul, .ai-content ol ol, .ai-content ul ol, .ai-content ol ul {
  margin: 0;
  padding-left: 20px;
}

.ai-content ul ul {
  list-style-type: circle;
}

.ai-content ul ul ul {
  list-style-type: square;
}

/* 引用块样式 */
.ai-content blockquote {
  margin: 8px 0;
  padding: 0 16px;
  color: #6a737d;
  border-left: 4px solid #dfe2e5;
  background-color: #f6f8fa;
  border-radius: 3px;
}

.ai-content blockquote p {
  margin: 8px 0;
}

/* 代码样式 */
.ai-content code {
  padding: 2px 4px;
  background-color: #f6f8fa;
  border-radius: 3px;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 85%;
  color: #e83e8c;
}

.ai-content pre {
  margin: 8px 0;
  padding: 12px;
  background-color: #f6f8fa;
  border-radius: 6px;
  overflow-x: auto;
}

.ai-content pre code {
  padding: 0;
  background-color: transparent;
  border-radius: 0;
  color: #24292e;
}

/* 保持换行和格式 */
.ai-content div {
  line-height: 1.6;
  /* 移除 white-space: pre-wrap，因为现在使用<br>标签处理换行 */
}

/* 消息内容通用样式 */
.message-content {
  padding: 10px 15px;
  border-radius: 18px;
  line-height: 1.4;
  font-size: 14px;
}

/* 消息时间样式 */
.message-time {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  text-align: center;
}

/* 加载消息样式 */
.loading-message {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  opacity: 0.8;
}

/* 输入中动画 */
.typing-indicator {
  display: flex;
  gap: 3px;
  margin-bottom: 8px;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #409EFF;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区域样式 */
.chat-input-area {
  border-top: 1px solid #e4e7ed;
  padding-top: 15px;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a1a1a1;
}

/* 消息内容格式化 */
.message-content >>> strong {
  font-weight: bold;
}

.message-content >>> em {
  font-style: italic;
}

.message-content >>> br {
  line-height: 1.8;
}

/* 让dict-tag在表格列中撑满宽度 */
.full-width-dict-tag .cell {
  display: flex !important;
  justify-content: center;
}

.full-width-dict-tag .cell > div {
  width: 100%;
}

.full-width-dict-tag .cell .el-tag,
.full-width-dict-tag .cell span {
  width: 100%;
  text-align: center;
  display: inline-block;
}
</style>
