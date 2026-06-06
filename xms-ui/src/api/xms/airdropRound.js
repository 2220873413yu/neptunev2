import request from '@/utils/request'

// 查询空投轮次配置列表
export function listAirdropRound(query) {
  return request({
    url: '/xms/airdropRound/list',
    method: 'get',
    params: query
  })
}

// 查询空投轮次配置详细
export function getAirdropRound(id) {
  return request({
    url: '/xms/airdropRound/' + id,
    method: 'get'
  })
}

// 新增空投轮次配置
export function addAirdropRound(data) {
  return request({
    url: '/xms/airdropRound',
    method: 'post',
    data: data
  })
}

// 修改空投轮次配置
export function updateAirdropRound(data) {
  return request({
    url: '/xms/airdropRound',
    method: 'put',
    data: data
  })
}

// 删除空投轮次配置
export function delAirdropRound(id) {
  return request({
    url: '/xms/airdropRound/' + id,
    method: 'delete'
  })
}
