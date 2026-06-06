import request from '@/utils/request'

// 查询用户推荐奖配置列表
export function listUserRecommendRewardConfig(query) {
  return request({
    url: '/xms/userRecommendRewardConfig/list',
    method: 'get',
    params: query
  })
}

// 查询用户推荐奖配置详细
export function getUserRecommendRewardConfig(id) {
  return request({
    url: '/xms/userRecommendRewardConfig/' + id,
    method: 'get'
  })
}

// 新增用户推荐奖配置
export function addUserRecommendRewardConfig(data) {
  return request({
    url: '/xms/userRecommendRewardConfig',
    method: 'post',
    data: data
  })
}

// 修改用户推荐奖配置
export function updateUserRecommendRewardConfig(data) {
  return request({
    url: '/xms/userRecommendRewardConfig',
    method: 'put',
    data: data
  })
}

// 删除用户推荐奖配置
export function delUserRecommendRewardConfig(id) {
  return request({
    url: '/xms/userRecommendRewardConfig/' + id,
    method: 'delete'
  })
}
