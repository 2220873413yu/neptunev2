import request from '@/utils/request'

// 查询用户等级考核配置列表
export function listW3UserLevelConfig(query) {
  return request({
    url: '/xms/w3UserLevelConfig/list',
    method: 'get',
    params: query
  })
}

// 查询用户等级考核配置详细
export function getW3UserLevelConfig(id) {
  return request({
    url: '/xms/w3UserLevelConfig/' + id,
    method: 'get'
  })
}

// 新增用户等级考核配置
export function addW3UserLevelConfig(data) {
  return request({
    url: '/xms/w3UserLevelConfig',
    method: 'post',
    data: data
  })
}

// 修改用户等级考核配置
export function updateW3UserLevelConfig(data) {
  return request({
    url: '/xms/w3UserLevelConfig',
    method: 'put',
    data: data
  })
}

// 删除用户等级考核配置
export function delW3UserLevelConfig(id) {
  return request({
    url: '/xms/w3UserLevelConfig/' + id,
    method: 'delete'
  })
}
