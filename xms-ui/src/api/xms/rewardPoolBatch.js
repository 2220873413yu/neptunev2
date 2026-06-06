import request from '@/utils/request'

// 查询分红批次记录列表
export function listRewardPoolBatch(query) {
  return request({
    url: '/xms/rewardPoolBatch/list',
    method: 'get',
    params: query
  })
}

// 查询分红批次记录详细
export function getRewardPoolBatch(id) {
  return request({
    url: '/xms/rewardPoolBatch/' + id,
    method: 'get'
  })
}

// 新增分红批次记录
export function addRewardPoolBatch(data) {
  return request({
    url: '/xms/rewardPoolBatch',
    method: 'post',
    data: data
  })
}

// 修改分红批次记录
export function updateRewardPoolBatch(data) {
  return request({
    url: '/xms/rewardPoolBatch',
    method: 'put',
    data: data
  })
}

// 删除分红批次记录
export function delRewardPoolBatch(id) {
  return request({
    url: '/xms/rewardPoolBatch/' + id,
    method: 'delete'
  })
}
