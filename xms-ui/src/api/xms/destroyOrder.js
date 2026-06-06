import request from '@/utils/request'

// 查询销毁记录列表
export function listDestroyOrder(query) {
  return request({
    url: '/xms/destroyOrder/list',
    method: 'get',
    params: query
  })
}

// 查询销毁记录详细
export function getDestroyOrder(id) {
  return request({
    url: '/xms/destroyOrder/' + id,
    method: 'get'
  })
}

// 新增销毁记录
export function addDestroyOrder(data) {
  return request({
    url: '/xms/destroyOrder',
    method: 'post',
    data: data
  })
}

// 修改销毁记录
export function updateDestroyOrder(data) {
  return request({
    url: '/xms/destroyOrder',
    method: 'put',
    data: data
  })
}

// 删除销毁记录
export function delDestroyOrder(id) {
  return request({
    url: '/xms/destroyOrder/' + id,
    method: 'delete'
  })
}

// 销毁订单统计
export function getDestroyOrderStatistics() {
  return request({
    url: '/xms/destroyOrder/statistics',
    method: 'get'
  })
}