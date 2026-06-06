import request from '@/utils/request'

// 查询交易产品行情数据管理列表
export function listMarketTradeConfig(query) {
  return request({
    url: '/xms/marketTradeConfig/list',
    method: 'get',
    params: query
  })
}

// 查询交易产品行情数据管理详细
export function getMarketTradeConfig(id) {
  return request({
    url: '/xms/marketTradeConfig/' + id,
    method: 'get'
  })
}

// 是否订阅盘口
export function handleDataPankou(id, isPankou) {
  return request({
    url: '/xms/marketTradeConfig/handleDataPankou',
    method: 'get',
    params: {
      id: id,
      isPankou: isPankou
    }
  })
}

// 新增交易产品行情数据管理
export function addMarketTradeConfig(data) {
  return request({
    url: '/xms/marketTradeConfig',
    method: 'post',
    data: data
  })
}

// 修改交易产品行情数据管理
export function updateMarketTradeConfig(data) {
  return request({
    url: '/xms/marketTradeConfig',
    method: 'put',
    data: data
  })
}

// 删除交易产品行情数据管理
export function delMarketTradeConfig(id) {
  return request({
    url: '/xms/marketTradeConfig/' + id,
    method: 'delete'
  })
}
