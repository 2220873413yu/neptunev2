import request from '@/utils/request'

// 查询层奖配置列表
export function listUserInvestLayerConfig(query) {
  return request({
    url: '/xms/userInvestLayerConfig/list',
    method: 'get',
    params: query
  })
}

// 查询层奖配置详细
export function getUserInvestLayerConfig(id) {
  return request({
    url: '/xms/userInvestLayerConfig/' + id,
    method: 'get'
  })
}

// 新增层奖配置
export function addUserInvestLayerConfig(data) {
  return request({
    url: '/xms/userInvestLayerConfig',
    method: 'post',
    data: data
  })
}

// 修改层奖配置
export function updateUserInvestLayerConfig(data) {
  return request({
    url: '/xms/userInvestLayerConfig',
    method: 'put',
    data: data
  })
}

// 删除层奖配置
export function delUserInvestLayerConfig(id) {
  return request({
    url: '/xms/userInvestLayerConfig/' + id,
    method: 'delete'
  })
}
