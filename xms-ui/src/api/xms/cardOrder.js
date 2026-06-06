import request from '@/utils/request'

// 查询卡片订单列表
export function listCardOrder(query) {
  return request({
    url: '/xms/cardOrder/list',
    method: 'get',
    params: query
  })
}

// 查询卡片订单详细
export function getCardOrder(id) {
  return request({
    url: '/xms/cardOrder/' + id,
    method: 'get'
  })
}

// 新增卡片订单
export function addCardOrder(data) {
  return request({
    url: '/xms/cardOrder',
    method: 'post',
    data: data
  })
}

// 修改卡片订单
export function updateCardOrder(data) {
  return request({
    url: '/xms/cardOrder',
    method: 'put',
    data: data
  })
}

// 删除卡片订单
export function delCardOrder(id) {
  return request({
    url: '/xms/cardOrder/' + id,
    method: 'delete'
  })
}
