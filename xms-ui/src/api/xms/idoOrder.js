import request from '@/utils/request'

// 查询ido 订单列表
export function listIdoOrder(query) {
  return request({
    url: '/xms/idoOrder/list',
    method: 'get',
    params: query
  })
}

// 查询ido 订单详细
export function getIdoOrder(id) {
  return request({
    url: '/xms/idoOrder/' + id,
    method: 'get'
  })
}

// 新增ido 订单
export function addIdoOrder(data) {
  return request({
    url: '/xms/idoOrder',
    method: 'post',
    data: data
  })
}

// 修改ido 订单
export function updateIdoOrder(data) {
  return request({
    url: '/xms/idoOrder',
    method: 'put',
    data: data
  })
}

// 删除ido 订单
export function delIdoOrder(id) {
  return request({
    url: '/xms/idoOrder/' + id,
    method: 'delete'
  })
}
