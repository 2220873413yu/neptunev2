import request from '@/utils/request'

// 查询用户激活订单列表
export function listActiveOrder(query) {
  return request({
    url: '/xms/activeOrder/list',
    method: 'get',
    params: query
  })
}

// 查询用户激活订单详细
export function getActiveOrder(id) {
  return request({
    url: '/xms/activeOrder/' + id,
    method: 'get'
  })
}

// 新增用户激活订单
export function addActiveOrder(data) {
  return request({
    url: '/xms/activeOrder',
    method: 'post',
    data: data
  })
}

// 修改用户激活订单
export function updateActiveOrder(data) {
  return request({
    url: '/xms/activeOrder',
    method: 'put',
    data: data
  })
}

// 删除用户激活订单
export function delActiveOrder(id) {
  return request({
    url: '/xms/activeOrder/' + id,
    method: 'delete'
  })
}
