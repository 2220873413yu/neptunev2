import request from '@/utils/request'

// 查询保险仓释放订单列表
export function listInsuranceOrder(query) {
  return request({
    url: '/xms/insuranceOrder/list',
    method: 'get',
    params: query
  })
}

// 查询保险仓释放订单详细
export function getInsuranceOrder(id) {
  return request({
    url: '/xms/insuranceOrder/' + id,
    method: 'get'
  })
}

// 新增保险仓释放订单
export function addInsuranceOrder(data) {
  return request({
    url: '/xms/insuranceOrder',
    method: 'post',
    data: data
  })
}

// 修改保险仓释放订单
export function updateInsuranceOrder(data) {
  return request({
    url: '/xms/insuranceOrder',
    method: 'put',
    data: data
  })
}

// 删除保险仓释放订单
export function delInsuranceOrder(id) {
  return request({
    url: '/xms/insuranceOrder/' + id,
    method: 'delete'
  })
}
