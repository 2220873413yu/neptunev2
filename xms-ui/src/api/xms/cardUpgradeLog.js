import request from '@/utils/request'

// 查询卡片升级日志列表
export function listCardUpgradeLog(query) {
  return request({
    url: '/xms/cardUpgradeLog/list',
    method: 'get',
    params: query
  })
}

// 查询卡片升级日志详细
export function getCardUpgradeLog(id) {
  return request({
    url: '/xms/cardUpgradeLog/' + id,
    method: 'get'
  })
}

// 新增卡片升级日志
export function addCardUpgradeLog(data) {
  return request({
    url: '/xms/cardUpgradeLog',
    method: 'post',
    data: data
  })
}

// 修改卡片升级日志
export function updateCardUpgradeLog(data) {
  return request({
    url: '/xms/cardUpgradeLog',
    method: 'put',
    data: data
  })
}

// 删除卡片升级日志
export function delCardUpgradeLog(id) {
  return request({
    url: '/xms/cardUpgradeLog/' + id,
    method: 'delete'
  })
}
