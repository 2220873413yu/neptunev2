import request from '@/utils/request'

// 查询AI分析行情列表
export function listAiMarketInsight(query) {
  return request({
    url: '/xms/aiMarketInsight/list',
    method: 'get',
    params: query
  })
}

// 查询AI分析行情详细
export function getAiMarketInsight(id) {
  return request({
    url: '/xms/aiMarketInsight/' + id,
    method: 'get'
  })
}

// 新增AI分析行情
export function addAiMarketInsight(data) {
  return request({
    url: '/xms/aiMarketInsight',
    method: 'post',
    data: data
  })
}

// 修改AI分析行情
export function updateAiMarketInsight(data) {
  return request({
    url: '/xms/aiMarketInsight',
    method: 'put',
    data: data
  })
}

// 删除AI分析行情
export function delAiMarketInsight(id) {
  return request({
    url: '/xms/aiMarketInsight/' + id,
    method: 'delete'
  })
}
