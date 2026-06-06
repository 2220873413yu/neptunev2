import request from '@/utils/request'

// 查询平台币每日价格列表
export function listPtbDailyPrice(query) {
  return request({
    url: '/xms/ptbDailyPrice/list',
    method: 'get',
    params: query
  })
}

// 查询平台币每日价格详细
export function getPtbDailyPrice(id) {
  return request({
    url: '/xms/ptbDailyPrice/' + id,
    method: 'get'
  })
}

// 新增平台币每日价格
export function addPtbDailyPrice(data) {
  return request({
    url: '/xms/ptbDailyPrice',
    method: 'post',
    data: data
  })
}

// 修改平台币每日价格
export function updatePtbDailyPrice(data) {
  return request({
    url: '/xms/ptbDailyPrice',
    method: 'put',
    data: data
  })
}

// 删除平台币每日价格
export function delPtbDailyPrice(id) {
  return request({
    url: '/xms/ptbDailyPrice/' + id,
    method: 'delete'
  })
}
