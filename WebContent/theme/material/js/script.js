
$(document).ready(function() {
  setupProjects();
  buildMenu();

  //============= material-kit.js ======================
  BrowserDetect.init();

  // Init Material scripts for buttons ripples, inputs animations etc, more info on the next link https://github.com/FezVrasta/bootstrap-material-design#materialjs
  $('body').bootstrapMaterialDesign();

  window_width = $(window).width();

  $navbar = $('.navbar[color-on-scroll]');
  scroll_distance = $navbar.attr('color-on-scroll') || 500;

  $navbar_collapse = $('.navbar').find('.navbar-collapse');

  //  Activate the Tooltips
  $('[data-toggle="tooltip"], [rel="tooltip"]').tooltip();

  // Activate Popovers
  $('[data-toggle="popover"]').popover();

  if ($('.navbar-color-on-scroll').length != 0) {
    $(window).on('scroll', materialKit.checkScrollForTransparentNavbar);
  }

  materialKit.checkScrollForTransparentNavbar();

  if (window_width >= 768) {
    big_image = $('.page-header[data-parallax="true"]');
    if (big_image.length != 0) {
      $(window).on('scroll', materialKit.checkScrollForParallax);
    }

  }
  //============= material-dashboard.js ======================
  $('body').bootstrapMaterialDesign();

  $sidebar = $('.sidebar');

  md.initSidebarsCheck();

  window_width = $(window).width();

  // check if there is an image set for the sidebar's background
  md.checkSidebarImage();

  //    Activate bootstrap-select
  if ($(".selectpicker").length != 0) {
    $(".selectpicker").selectpicker();
  }

  //  Activate the tooltips
  $('[rel="tooltip"]').tooltip();

  $('.form-control').on("focus", function() {
    $(this).parent('.input-group').addClass("input-group-focus");
  }).on("blur", function() {
    $(this).parent(".input-group").removeClass("input-group-focus");
  });

  // remove class has-error for checkbox validation
  $('input[type="checkbox"][required="true"], input[type="radio"][required="true"]').on('click', function() {
    if ($(this).hasClass('error')) {
      $(this).closest('div').removeClass('has-error');
    }
  });
});

function buildMenu() {
  var sideBarMenu = document.getElementById("sideBarMenu");
  var ul = document.createElement('ul');
  ul.className = 'nav';
  for (var i = 0; i < menuArr.length; i++) {
    var menuItem = menuArr[i];
    if (menuItem.projectPk) {
      ul.appendChild(createProjectMenu(menuItem));
    }
    else if (menuItem.children && menuItem.children.length > 0) {
      ul.appendChild(createMenuWthChildren(menuItem));
    }
    else {
      ul.appendChild(createMenuItem(menuItem));
    }
  }
  sideBarMenu.appendChild(ul);
}

function createMenuWthChildren(item) {
  var li = null;
  if (item.children && item.children.length > 0) {
    li = createCollapsibleMenu(item);
    var div = document.createElement('div');
    var ul = document.createElement('ul');
    ul.className = 'nav';
    div.className = 'collapse' + (item.projectPk === projectPk ? ' show' : '');
    for (var i = 0; i < item.children.length; i++) {
      ul.appendChild(createMenuWthChildren(item.children[i]));
    }
    div.appendChild(ul);
    li.appendChild(div);
  }
  else {
    li = createMenuItem(item, 1);
  }
  return li;
}
function createProjectMenu(project) {
  var li = createCollapsibleMenu(project);
  var div = document.createElement('div');
  var ul = document.createElement('ul');
  var pk = project.projectPk;
  div.className = 'collapse' + (pk === projectPk ? ' show' : '');
  ul.className = 'nav';

  ul.appendChild(createMenuItem({ label: "Dashboard", icon: 'assignment_turned_in', url: '/dashboard.do?projectPk=' + pk }, 1));
  if (project.all) {
    ul.appendChild(createMenuItem({ label: "ATO Updates", icon: 'assignment_turned_in', url: '/support.do?action=issueList&projectPk=' + pk }, 1));
    ul.appendChild(createMenuItem({ label: "Support Visit Calendar", icon: 'assignment_turned_in', url: '/support.do?action=shipVisitCalendar&projectPk=' + pk }, 1));
  }
  ul.appendChild(createMenuItem({ label: "POC List", icon: 'assignment_turned_in', url: '/user.do?action=pocList&projectPk=' + pk }, 1));
  ul.appendChild(createMenuItem({ label: "Support Issues", icon: 'assignment_turned_in', url: '/support.do?action=issueList&projectPk=' + pk }, 1));
  ul.appendChild(createMenuItem({ label: "Task List", icon: 'assignment_turned_in', url: '/project.do?action=taskList&projectPk=' + pk }, 1));
  div.className = 'collapse';
  div.appendChild(ul);
  li.appendChild(div);
  return li;
}

function createMenuItem(item, indent) {
  var li = document.createElement('li');
  var a = document.createElement('a');
  li.className = 'nav-item';
  li.style = indent ? 'padding-left: 25px;' : '';
  a.className = 'nav-link';
  a.innerHTML = '<i class="material-icons">' + item.icon + '</i> ' + item.label;
  a.href = item.url ? item.url : '#';
  li.appendChild(a);
  return li;
}

function createCollapsibleMenu(item) {
  var li = document.createElement('li');
  var a = document.createElement('a');
  li.className = 'nav-item';
  a.className = 'nav-link';
  a.setAttribute('data-toggle', 'collapse');
  a.setAttribute('aria-expanded', 'true');
  a.className = 'nav-link' + (item.projectPk === projectPk ? '' : ' collapsed');
  a.innerHTML = '<i class="material-icons">' + item.icon + '</i><p>' + item.label + ' <b class="caret"></b></p>';
  a.onclick = () => {
    var sibling = a.nextSibling;
    sibling.className = sibling.className === 'collapse' ? '' : 'collapse';
  };
  a.href = item.url ? item.url : '#';
  li.appendChild(a);
  return li;
}

function setupProjects() {
  var facet = findProject('1');
  facet.all = true;

  var facetCarriers = findProject('24');
  facetCarriers.taskList = true;
  facetCarriers.pocList = true;
  facetCarriers.supportIssues = true;

  var logisticsMonthlyReports = findProject('19');
  facetCarriers.taskList = true;
  facetCarriers.pocList = true;
  facetCarriers.supportIssues = true;

  var facetFleetForces = findProject('22');
  facetFleetForces.taskList = true;
  facetFleetForces.pocList = true;
  facetFleetForces.supportIssues = true;

  var paci = findProject('26');
  paci.taskList = true;
  paci.pocList = true;
  paci.supportIssues = true;

  var nmc = findProject('17');
  nmc.taskList = true;
  nmc.pocList = true;
  nmc.supportIssues = true;
}

function findProject(projectPk) {
  for (var i = 0; i < menuArr.length; i++) {
    if (menuArr[i].children) {
      for (var j = 0; j < menuArr[i].children.length; j++) {
        if (menuArr[i].children[j].projectPk === projectPk) {
          return menuArr[i].children[j];
        }
      }
    }
  }
  return {};
}