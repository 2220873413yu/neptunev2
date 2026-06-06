import request from '@/utils/request'

// 查询每日质押数据快照列表
export function listStakeDailySnapshot(query) {
  return request({
    url: '/xms/stakeDailySnapshot/list',
    method: 'get',
    params: query
  })
}

// 查询每日质押数据快照详细
export function getStakeDailySnapshot(id) {
  return request({
    url: '/xms/stakeDailySnapshot/' + id,
    method: 'get'
  })
}

// 新增每日质押数据快照
export function addStakeDailySnapshot(data) {
  return request({
    url: '/xms/stakeDailySnapshot',
    method: 'post',
    data: data
  })
}

// 修改每日质押数据快照
export function updateStakeDailySnapshot(data) {
  return request({
    url: '/xms/stakeDailySnapshot',
    method: 'put',
    data: data
  })
}

// 删除每日质押数据快照
export function delStakeDailySnapshot(id) {
  return request({
    url: '/xms/stakeDailySnapshot/' + id,
    method: 'delete'
  })
}
