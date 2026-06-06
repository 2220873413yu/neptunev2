import request from '@/utils/request'

// 查询购买H代币订单列表
export function listBuyHOrder(query) {
  return request({
    url: '/xms/buyHOrder/list',
    method: 'get',
    params: query
  })
}

// 查询购买H代币订单详细
export function getBuyHOrder(id) {
  return request({
    url: '/xms/buyHOrder/' + id,
    method: 'get'
  })
}

// 新增购买H代币订单
export function addBuyHOrder(data) {
  return request({
    url: '/xms/buyHOrder',
    method: 'post',
    data: data
  })
}

// 修改购买H代币订单
export function updateBuyHOrder(data) {
  return request({
    url: '/xms/buyHOrder',
    method: 'put',
    data: data
  })
}

// 删除购买H代币订单
export function delBuyHOrder(id) {
  return request({
    url: '/xms/buyHOrder/' + id,
    method: 'delete'
  })
}
