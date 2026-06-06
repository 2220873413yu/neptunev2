import request from '@/utils/request'

// 查询互动奖比例配置列表
export function listInteractRewardConfig(query) {
  return request({
    url: '/xms/interactRewardConfig/list',
    method: 'get',
    params: query
  })
}

// 查询互动奖比例配置详细
export function getInteractRewardConfig(id) {
  return request({
    url: '/xms/interactRewardConfig/' + id,
    method: 'get'
  })
}

// 新增互动奖比例配置
export function addInteractRewardConfig(data) {
  return request({
    url: '/xms/interactRewardConfig',
    method: 'post',
    data: data
  })
}

// 修改互动奖比例配置
export function updateInteractRewardConfig(data) {
  return request({
    url: '/xms/interactRewardConfig',
    method: 'put',
    data: data
  })
}

// 删除互动奖比例配置
export function delInteractRewardConfig(id) {
  return request({
    url: '/xms/interactRewardConfig/' + id,
    method: 'delete'
  })
}
