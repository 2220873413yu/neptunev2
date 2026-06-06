import request from '@/utils/request'

// 查询认购节点配置列表
export function listNodePlan(query) {
  return request({
    url: '/xms/nodePlan/list',
    method: 'get',
    params: query
  })
}

// 查询认购节点配置详细
export function getNodePlan(id) {
  return request({
    url: '/xms/nodePlan/' + id,
    method: 'get'
  })
}

// 新增认购节点配置
export function addNodePlan(data) {
  return request({
    url: '/xms/nodePlan',
    method: 'post',
    data: data
  })
}

// 修改认购节点配置
export function updateNodePlan(data) {
  return request({
    url: '/xms/nodePlan',
    method: 'put',
    data: data
  })
}

// 删除认购节点配置
export function delNodePlan(id) {
  return request({
    url: '/xms/nodePlan/' + id,
    method: 'delete'
  })
}
