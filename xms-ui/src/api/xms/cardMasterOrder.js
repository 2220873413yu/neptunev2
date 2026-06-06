import request from '@/utils/request'

// 查询购买记录列表
export function listCardMasterOrder(query) {
  return request({
    url: '/xms/cardMasterOrder/list',
    method: 'get',
    params: query
  })
}

// 查询购买记录详细
export function getCardMasterOrder(id) {
  return request({
    url: '/xms/cardMasterOrder/' + id,
    method: 'get'
  })
}

// 新增购买记录
export function addCardMasterOrder(data) {
  return request({
    url: '/xms/cardMasterOrder',
    method: 'post',
    data: data
  })
}

// 修改购买记录
export function updateCardMasterOrder(data) {
  return request({
    url: '/xms/cardMasterOrder',
    method: 'put',
    data: data
  })
}

// 删除购买记录
export function delCardMasterOrder(id) {
  return request({
    url: '/xms/cardMasterOrder/' + id,
    method: 'delete'
  })
}
