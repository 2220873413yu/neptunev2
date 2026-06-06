import request from '@/utils/request'

// 查询BOOMAI日利率调节规则列表
export function listDayRatioRule(query) {
  return request({
    url: '/xms/dayRatioRule/list',
    method: 'get',
    params: query
  })
}

// 查询BOOMAI日利率调节规则详细
export function getDayRatioRule(id) {
  return request({
    url: '/xms/dayRatioRule/' + id,
    method: 'get'
  })
}

// 新增BOOMAI日利率调节规则
export function addDayRatioRule(data) {
  return request({
    url: '/xms/dayRatioRule',
    method: 'post',
    data: data
  })
}

// 修改BOOMAI日利率调节规则
export function updateDayRatioRule(data) {
  return request({
    url: '/xms/dayRatioRule',
    method: 'put',
    data: data
  })
}

// 删除BOOMAI日利率调节规则
export function delDayRatioRule(id) {
  return request({
    url: '/xms/dayRatioRule/' + id,
    method: 'delete'
  })
}
