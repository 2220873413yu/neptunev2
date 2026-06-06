import request from '@/utils/request'

// 查询用户节点订单列表
export function listNodePlanOrder(query) {
  return request({
    url: '/xms/nodePlanOrder/list',
    method: 'get',
    params: query
  })
}

// 查询用户节点订单详细
export function getNodePlanOrder(id) {
  return request({
    url: '/xms/nodePlanOrder/' + id,
    method: 'get'
  })
}

// 新增用户节点订单
export function addNodePlanOrder(data) {
  return request({
    url: '/xms/nodePlanOrder',
    method: 'post',
    data: data
  })
}

// 修改用户节点订单
export function updateNodePlanOrder(data) {
  return request({
    url: '/xms/nodePlanOrder',
    method: 'put',
    data: data
  })
}

// 删除用户节点订单
export function delNodePlanOrder(id) {
  return request({
    url: '/xms/nodePlanOrder/' + id,
    method: 'delete'
  })
}
