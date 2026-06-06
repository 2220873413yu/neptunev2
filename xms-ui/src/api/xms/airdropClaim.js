import request from '@/utils/request'

// 查询空投领取记录列表
export function listAirdropClaim(query) {
  return request({
    url: '/xms/airdropClaim/list',
    method: 'get',
    params: query
  })
}

// 查询空投领取记录详细
export function getAirdropClaim(id) {
  return request({
    url: '/xms/airdropClaim/' + id,
    method: 'get'
  })
}

// 新增空投领取记录
export function addAirdropClaim(data) {
  return request({
    url: '/xms/airdropClaim',
    method: 'post',
    data: data
  })
}

// 修改空投领取记录
export function updateAirdropClaim(data) {
  return request({
    url: '/xms/airdropClaim',
    method: 'put',
    data: data
  })
}

// 删除空投领取记录
export function delAirdropClaim(id) {
  return request({
    url: '/xms/airdropClaim/' + id,
    method: 'delete'
  })
}
