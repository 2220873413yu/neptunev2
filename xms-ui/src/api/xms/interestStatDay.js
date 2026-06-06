import request from '@/utils/request'

// 查询每日利息汇总列表
export function listInterestStatDay(query) {
  return request({
    url: '/xms/interestStatDay/list',
    method: 'get',
    params: query
  })
}

// 查询每日利息汇总详细
export function getInterestStatDay(id) {
  return request({
    url: '/xms/interestStatDay/' + id,
    method: 'get'
  })
}

// 新增每日利息汇总
export function addInterestStatDay(data) {
  return request({
    url: '/xms/interestStatDay',
    method: 'post',
    data: data
  })
}

// 修改每日利息汇总
export function updateInterestStatDay(data) {
  return request({
    url: '/xms/interestStatDay',
    method: 'put',
    data: data
  })
}

// 删除每日利息汇总
export function delInterestStatDay(id) {
  return request({
    url: '/xms/interestStatDay/' + id,
    method: 'delete'
  })
}
