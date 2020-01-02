

<%--$menuItems = array(
  array("label"=> "Dashboard", "page" => "dashboard", "icon" => "dashboard"),
  array("label"=> "User Profile", "page" => "user", "icon" => "person"),
  array("label"=> "Table List", "page" => "tables", "icon" => "content_paste"),
  array("label"=> "Calendar", "page" => "calendar", "icon" => "calendar"),
  array("label"=> "Typography", "page" => "typography", "icon" => "library_books"),
  array("label"=> "Icons", "page" => "icons", "icon" => "bubble_chart"),
  //array("label"=> "Maps", "page" => "map", "icon" => "location_ons"),
  array("label"=> "Notifications", "page" => "notifications", "icon" => "notifications"),
  array("label"=> "Login", "page" => "login", "icon" => "person"),
  array("label"=> "Form Components", "page" => "forms", "icon" => "input")
  //array("label"=> "Upgrade to PRO", "page" => "upgrade", "icon" => "unarchive")
  
  <?php foreach ($menuItems as &$menuItem): ?>
    <li class="nav-item <?=$page == $menuItem["label"] ? 'active' : ''?>">
      <a class="nav-link" href="/pshi-tracker-design/examples/<?=$menuItem["page"]?>.php">
        <i class="material-icons"><?=$menuItem["icon"]?></i>
        <p><?=$menuItem["label"]?></p>
      </a>
    </li>
  <?php endforeach; ?>
); --%>
<div class="sidebar" data-background-color="white" data-image="/theme/material/img/sidebar-1.jpg">
  <!--
    Tip 1: You can change the color of the sidebar using: data-color="purple | azure | green | orange | danger"
    Tip 2: you can also add an image using data-image tag
   -->
  <div class="logo">
    <a href="/menu.do" class="simple-text logo-normal">
      Premier Solutions Hi, LLC
    </a>
  </div>
  <div id="sideBarMenu" class="sidebar-wrapper">
    <!-- 
    <ul class="nav">
        <li class="nav-item active">
          <a class="nav-link" href="/pshi-tracker-design/examples/dashboard.php">
            <i class="material-icons">dashboard</i>
            Dashboard
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/user.php">
            <i class="material-icons">person</i>
            User Profile</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/tables.php">
            <i class="material-icons">content_paste</i>
            <p>Table List</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/calendar.php">
            <i class="material-icons">calendar</i>
            <p>Calendar</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/typography.php">
            <i class="material-icons">library_books</i>
            <p>Typography</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/icons.php">
            <i class="material-icons">bubble_chart</i>
            <p>Icons</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/notifications.php">
            <i class="material-icons">notifications</i>
            <p>Notifications</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/login.php">
            <i class="material-icons">person</i>
            <p>Login</p>
          </a>
        </li>
        <li class="nav-item ">
          <a class="nav-link" href="/pshi-tracker-design/examples/forms.php">
            <i class="material-icons">input</i>
            <p>Form Components</p>
          </a>
        </li>
              
      <li class="nav-item ">
        <a class="nav-link" href="https://demos.creative-tim.com/material-dashboard-pro/examples/tables/datatables.net.html">
          <i class="material-icons">dashboard</i>
          <p>Pro Examples</p>
        </a>
      </li>


      <li class="nav-item ">
        <a class="nav-link" href="https://demos.creative-tim.com/material-dashboard-pro/examples/tables/datatables.net.html">
          <i class="material-icons">dashboard</i>
          <p>Pro Examples</p>
        </a>
      </li>
      <li class="nav-item ">
        <a class="nav-link" data-toggle="collapse" href="#pagesExamples" aria-expanded="true">
          <i class="material-icons">image</i>
          <p> Pages 
             <b class="caret"></b>
          </p>
        </a>

        <div class="collapse " id="pagesExamples" style="">
          <ul class="nav">
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/pricing.html">
                <span class="sidebar-mini"> P </span>
                <span class="sidebar-normal"> Pricing </span>
              </a>
            </li>
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/rtl.html">
                <span class="sidebar-mini"> RS </span>
                <span class="sidebar-normal"> RTL Support </span>
              </a>
            </li>
            <li class="nav-item ">
                <a class="nav-link" href="../examples/pages/timeline.html">
                  <span class="sidebar-mini"> T </span>
                  <span class="sidebar-normal"> Timeline </span>
                </a>
            </li>
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/login.html">
                <span class="sidebar-mini"> LP </span>
                <span class="sidebar-normal"> Login Page </span>
              </a>
            </li>
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/register.html">
                <span class="sidebar-mini"> RP </span>
                <span class="sidebar-normal"> Register Page </span>
              </a>
            </li>
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/lock.html">
                <span class="sidebar-mini"> LSP </span>
                <span class="sidebar-normal"> Lock Screen Page </span>
              </a>
            </li>
            <li class="nav-item ">
              <a class="nav-link" href="../examples/pages/user.html">
                <span class="sidebar-mini"> UP </span>
                <span class="sidebar-normal"> User Profile </span>
              </a>
            </li>
          </ul>
        </div>
      </li>
    </ul>
     -->
  </div>
</div>

<script type="text/javascript">
var projectPk = '${leftbar_resultBean.projectPk}';
var menuArr = [
  { label: "Home", icon: 'dashboard', url: "/menu.do" },
  { label: "Wiki", icon: 'dashboard', url: "/wiki" }
];
<logic:present name="sideCustomerList">
  <logic:notEmpty name="sideCustomerList">
    <logic:iterate id="customer" name="sideCustomerList" type="com.premiersolutionshi.common.domain.Customer">
      <bean:define id="projects" name="customer" property="projects" />
      <logic:iterate id="project" name="projects" type="com.premiersolutionshi.common.domain.Project">
        menuArr.push({ label: '${project.projectName}', icon: 'library_books', projectPk: '${project.id}' });
      </logic:iterate>
    </logic:iterate>
  </logic:notEmpty>
</logic:present>
menuArr.push({ label: 'Administration', icon: 'dashboard', children: [
  { label: 'Manage Projects', icon: 'dashboard', url: '' },
  { label: 'Manage Users', icon: 'dashboard', url: '' },
  { label: 'Manage UIC List', icon: 'dashboard', url: '' },
]})
menuArr.push({ label: 'User CP', icon: 'dashboard', children: [
  { label: 'Manage PTO/Travel', icon: 'dashboard', url: '' },
  { label: 'Change Password', icon: 'dashboard', url: '' },
] })
</script>