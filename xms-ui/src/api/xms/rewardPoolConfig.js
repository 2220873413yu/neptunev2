import request from '@/utils/request'

// 查询分红池配置列表
export function listRewardPoolConfig(query) {
  return request({
    url: '/xms/rewardPoolConfig/list',
    method: 'get',
    params: query
  })
}

// 查询分红池配置详细
export function getRewardPoolConfig(id) {
  return request({
    url: '/xms/rewardPoolConfig/' + id,
    method: 'get'
  })
}

// 新增分红池配置
export function addRewardPoolConfig(data) {
  return request({
    url: '/xms/rewardPoolConfig',
    method: 'post',
    data: data
  })
}

// 修改分红池配置
export function updateRewardPoolConfig(data) {
  return request({
    url: '/xms/rewardPoolConfig',
    method: 'put',
    data: data
  })
}

// 删除分红池配置
export function delRewardPoolConfig(id) {
  return request({
    url: '/xms/rewardPoolConfig/' + id,
    method: 'delete'
  })
}
