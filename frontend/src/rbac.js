export const RBAC_MATRIX = [
  { action: 'manageUsers', label: 'Manage users & assign roles', ADMIN: 'Yes', BRAND_MANAGER: 'No', CONTENT_CREATOR: 'No', REVIEWER: 'No' },
  { action: 'manageWorkspaces', label: 'Create & configure brand workspaces', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'No', REVIEWER: 'No' },
  { action: 'manageGuidelines', label: 'Edit guidelines, prohibited terms & tone', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'No', REVIEWER: 'No' },
  { action: 'manageSources', label: 'Manage approved sources & claims ledger', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'No', REVIEWER: 'No' },
  { action: 'submitContent', label: 'Submit content for review', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'Yes', REVIEWER: 'No' },
  { action: 'editIntentLock', label: 'Apply Intent Lock fixes & create revisions', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'Yes', REVIEWER: 'No' },
  { action: 'submitForApproval', label: 'Submit content for approval', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'Yes', REVIEWER: 'No' },
  { action: 'viewFindings', label: 'View review findings & citations', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'Yes', REVIEWER: 'Yes' },
  { action: 'requestChanges', label: 'Add review comments & request changes', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'No', REVIEWER: 'Yes' },
  { action: 'approveReject', label: 'Human approve or reject content', ADMIN: 'Yes', BRAND_MANAGER: 'No', CONTENT_CREATOR: 'No', REVIEWER: 'Yes' },
  { action: 'viewHeatmap', label: 'View Clarity Heatmap & conflict signals', ADMIN: 'Yes', BRAND_MANAGER: 'Yes', CONTENT_CREATOR: 'View-only', REVIEWER: 'View-only' }
];

const roleMap = { admin: 'ADMIN', agency: 'BRAND_MANAGER', creator: 'CONTENT_CREATOR', reviewer: 'REVIEWER' };
const actionRoles = {
  manageUsers: ['ADMIN'],
  manageWorkspaces: ['ADMIN', 'BRAND_MANAGER'],
  manageGuidelines: ['ADMIN', 'BRAND_MANAGER'],
  manageSources: ['ADMIN', 'BRAND_MANAGER'],
  submitContent: ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR'],
  editIntentLock: ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR'],
  submitForApproval: ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR'],
  viewFindings: ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR', 'REVIEWER'],
  requestChanges: ['ADMIN', 'BRAND_MANAGER', 'REVIEWER'],
  approveReject: ['ADMIN', 'REVIEWER'],
  viewHeatmap: ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR', 'REVIEWER']
};

export function canonicalRole(role) {
  return roleMap[role] || role || 'CONTENT_CREATOR';
}

export function canPerform(role, action) {
  const allowed = actionRoles[action] || [];
  return allowed.includes(canonicalRole(role));
}

export function isHeatmapReadOnly(role) {
  return ['CONTENT_CREATOR', 'REVIEWER'].includes(canonicalRole(role));
}

export function canAccessPage(role, page) {
  const key = canonicalRole(role);
  if (page === 'Team Management' || page === 'Settings') return key === 'ADMIN';
  if (page === 'Brand knowledge') return ['ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR', 'REVIEWER'].includes(key);
  if (page === 'Companies' || page === 'Products' || page === 'Overview') return true;
  if (page === 'Content review') return canPerform(key, 'submitContent');
  if (page === 'My submissions') return canPerform(key, 'submitForApproval');
  if (page === 'Approvals') return ['ADMIN', 'BRAND_MANAGER', 'REVIEWER'].includes(key);
  if (page === 'Insights') return canPerform(key, 'viewHeatmap');
  return false;
}
