



<!DOCTYPE html>
<html lang="en" class=" is-copy-enabled is-u2f-enabled">
  <head prefix="og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# object: http://ogp.me/ns/object# article: http://ogp.me/ns/article# profile: http://ogp.me/ns/profile#">
    <meta charset='utf-8'>
    

    <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/frameworks-999c004c5803f3a54bf0be8f3fbdb027bd0a69b9bf97743fbceb696ea8e8b267.css" integrity="sha256-mZwATFgD86VL8L6PP72wJ70Kabm/l3Q/vOtpbqjosmc=" media="all" rel="stylesheet" />
    <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/github-7360d92e05cc32ed5882e50f58e2b592cc826790dd2fcf8c3e9af0b84096ba1b.css" integrity="sha256-c2DZLgXMMu1YguUPWOK1ksyCZ5DdL8+MPprwuECWuhs=" media="all" rel="stylesheet" />
    
    
    
    

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Language" content="en">
    <meta name="viewport" content="width=device-width">
    
    <title>ku8eye/github-develop-guide.md at master · bestcloud/ku8eye</title>
    <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
    <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png">
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/apple-touch-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/apple-touch-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/apple-touch-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/apple-touch-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon-180x180.png">
    <meta property="fb:app_id" content="1401488693436528">

      <meta content="https://avatars0.githubusercontent.com/u/15244326?v=3&amp;s=400" name="twitter:image:src" /><meta content="@github" name="twitter:site" /><meta content="summary" name="twitter:card" /><meta content="bestcloud/ku8eye" name="twitter:title" /><meta content="Contribute to ku8eye development by creating an account on GitHub." name="twitter:description" />
      <meta content="https://avatars0.githubusercontent.com/u/15244326?v=3&amp;s=400" property="og:image" /><meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="bestcloud/ku8eye" property="og:title" /><meta content="https://github.com/bestcloud/ku8eye" property="og:url" /><meta content="Contribute to ku8eye development by creating an account on GitHub." property="og:description" />
      <meta name="browser-stats-url" content="https://api.github.com/_private/browser/stats">
    <meta name="browser-errors-url" content="https://api.github.com/_private/browser/errors">
    <link rel="assets" href="https://assets-cdn.github.com/">
    <link rel="web-socket" href="wss://live.github.com/_sockets/Njc4MjA0MTo5MTBjODg4NDExZDVlNTljYmE3ODYxYTVmNmYxNjRlMDpjOWJlMTRhOGY2YjA0NjZmYmE2ZjcwYTY5MmNiYjBjNjExOWNlMDFmNTA2OWY1ZDYyZWU4NzllZWE0MjcxZTJi--e3d0a5eb0ecfea7713e5a5616c7c5d08f28f90eb">
    <meta name="pjax-timeout" content="1000">
    <link rel="sudo-modal" href="/sessions/sudo_modal">
    <meta name="request-id" content="3DA43492:4CD5:10B57F1F:5833B23E" data-pjax-transient>

    <meta name="msapplication-TileImage" content="/windows-tile.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="selected-link" value="repo_source" data-pjax-transient>

    <meta name="google-site-verification" content="KT5gs8h0wvaagLKAVWq8bbeNwnZZK1r1XQysX3xurLU">
<meta name="google-site-verification" content="ZzhVyEFwb7w3e0-uOTltm8Jsck2F5StVihD0exw2fsA">
    <meta name="google-analytics" content="UA-3769691-2">

<meta content="collector.githubapp.com" name="octolytics-host" /><meta content="github" name="octolytics-app-id" /><meta content="3DA43492:4CD5:10B57F1F:5833B23E" name="octolytics-dimension-request_id" /><meta content="6782041" name="octolytics-actor-id" /><meta content="lbsg323" name="octolytics-actor-login" /><meta content="9abdef342a7ae70b9e825fd601cb31c3c88cb8d1dc9163efc3a41402a2c25d54" name="octolytics-actor-hash" />
<meta content="/&lt;user-name&gt;/&lt;repo-name&gt;/blob/show" data-pjax-transient="true" name="analytics-location" />



  <meta class="js-ga-set" name="dimension1" content="Logged In">



        <meta name="hostname" content="github.com">
    <meta name="user-login" content="lbsg323">

        <meta name="expected-hostname" content="github.com">
      <meta name="js-proxy-site-detection-payload" content="YzUxMjk2N2Y1MTI4NWYzYWIyNzkzZmRhZjUyY2E3YzA2MDBiZDBkMzYzMTNmN2FjMWM2ZDZhODk5ZjlhMWU0ZHx7InJlbW90ZV9hZGRyZXNzIjoiNjEuMTY0LjUyLjE0NiIsInJlcXVlc3RfaWQiOiIzREE0MzQ5Mjo0Q0Q1OjEwQjU3RjFGOjU4MzNCMjNFIiwidGltZXN0YW1wIjoxNDc5NzgyOTgwLCJob3N0IjoiZ2l0aHViLmNvbSJ9">


      <link rel="mask-icon" href="https://assets-cdn.github.com/pinned-octocat.svg" color="#000000">
      <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">

    <meta name="html-safe-nonce" content="6cb0fd3752e976cd41900532fece9caa4517ba28">
    <meta content="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" name="form-nonce" />

    <meta http-equiv="x-pjax-version" content="566c59f4e325cdb4cab15e8cbce268f9">
    

      
  <meta name="description" content="Contribute to ku8eye development by creating an account on GitHub.">
  <meta name="go-import" content="github.com/bestcloud/ku8eye git https://github.com/bestcloud/ku8eye.git">

  <meta content="15244326" name="octolytics-dimension-user_id" /><meta content="bestcloud" name="octolytics-dimension-user_login" /><meta content="44729925" name="octolytics-dimension-repository_id" /><meta content="bestcloud/ku8eye" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="44729925" name="octolytics-dimension-repository_network_root_id" /><meta content="bestcloud/ku8eye" name="octolytics-dimension-repository_network_root_nwo" />
  <link href="https://github.com/bestcloud/ku8eye/commits/master.atom" rel="alternate" title="Recent Commits to ku8eye:master" type="application/atom+xml">


      <link rel="canonical" href="https://github.com/bestcloud/ku8eye/blob/master/github-develop-guide.md" data-pjax-transient>
  </head>


  <body class="logged-in  env-production windows vis-public page-blob">
    <div id="js-pjax-loader-bar" class="pjax-loader-bar"><div class="progress"></div></div>
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>

    
    
    



        <div class="header header-logged-in true" role="banner">
  <div class="container clearfix">

    <a class="header-logo-invertocat" href="https://github.com/" data-hotkey="g d" aria-label="Homepage" data-ga-click="Header, go to dashboard, icon:logo">
  <svg aria-hidden="true" class="octicon octicon-mark-github" height="28" version="1.1" viewBox="0 0 16 16" width="28"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
</a>


        <div class="header-search scoped-search site-scoped-search js-site-search" role="search">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/bestcloud/ku8eye/search" class="js-site-search-form" data-scoped-search-url="/bestcloud/ku8eye/search" data-unscoped-search-url="/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <label class="form-control header-search-wrapper js-chromeless-input-container">
      <div class="header-search-scope">This repository</div>
      <input type="text"
        class="form-control header-search-input js-site-search-focus js-site-search-field is-clearable"
        data-hotkey="s"
        name="q"
        placeholder="Search"
        aria-label="Search this repository"
        data-unscoped-placeholder="Search GitHub"
        data-scoped-placeholder="Search"
        autocapitalize="off">
    </label>
</form></div>


      <ul class="header-nav float-left" role="navigation">
        <li class="header-nav-item">
          <a href="/pulls" aria-label="Pull requests you created" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:pulls context:user" data-hotkey="g p" data-selected-links="/pulls /pulls/assigned /pulls/mentioned /pulls">
            Pull requests
</a>        </li>
        <li class="header-nav-item">
          <a href="/issues" aria-label="Issues you created" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:issues context:user" data-hotkey="g i" data-selected-links="/issues /issues/assigned /issues/mentioned /issues">
            Issues
</a>        </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://gist.github.com/" data-ga-click="Header, go to gist, text:gist">Gist</a>
          </li>
      </ul>

    
<ul class="header-nav user-nav float-right" id="user-links">
  <li class="header-nav-item">
    
    <a href="/notifications" aria-label="You have unread notifications" class="header-nav-link notification-indicator tooltipped tooltipped-s js-socket-channel js-notification-indicator" data-channel="tenant:1:notification-changed:6782041" data-ga-click="Header, go to notifications, icon:unread" data-hotkey="g n">
        <span class="mail-status unread"></span>
        <svg aria-hidden="true" class="octicon octicon-bell" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 12v1H0v-1l.73-.58c.77-.77.81-2.55 1.19-4.42C2.69 3.23 6 2 6 2c0-.55.45-1 1-1s1 .45 1 1c0 0 3.39 1.23 4.16 5 .38 1.88.42 3.66 1.19 4.42l.66.58H14zm-7 4c1.11 0 2-.89 2-2H5c0 1.11.89 2 2 2z"/></svg>
</a>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link tooltipped tooltipped-s js-menu-target" href="/new"
       aria-label="Create new…"
       data-ga-click="Header, create new, icon:add">
      <svg aria-hidden="true" class="octicon octicon-plus float-left" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 9H7v5H5V9H0V7h5V2h2v5h5z"/></svg>
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <ul class="dropdown-menu dropdown-menu-sw">
        
<a class="dropdown-item" href="/new" data-ga-click="Header, create new repository">
  New repository
</a>

  <a class="dropdown-item" href="/new/import" data-ga-click="Header, import a repository">
    Import repository
  </a>

<a class="dropdown-item" href="https://gist.github.com/" data-ga-click="Header, create new gist">
  New gist
</a>

  <a class="dropdown-item" href="/organizations/new" data-ga-click="Header, create new organization">
    New organization
  </a>



  <div class="dropdown-divider"></div>
  <div class="dropdown-header">
    <span title="bestcloud/ku8eye">This repository</span>
  </div>
    <a class="dropdown-item" href="/bestcloud/ku8eye/issues/new" data-ga-click="Header, create new issue">
      New issue
    </a>

      </ul>
    </div>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link name tooltipped tooltipped-sw js-menu-target" href="/lbsg323"
       aria-label="View profile and more"
       data-ga-click="Header, show menu, icon:avatar">
      <img alt="@lbsg323" class="avatar" height="20" src="https://avatars0.githubusercontent.com/u/6782041?v=3&amp;s=40" width="20" />
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <div class="dropdown-menu dropdown-menu-sw">
        <div class="dropdown-header header-nav-current-user css-truncate">
          Signed in as <strong class="css-truncate-target">lbsg323</strong>
        </div>

        <div class="dropdown-divider"></div>

        <a class="dropdown-item" href="/lbsg323" data-ga-click="Header, go to profile, text:your profile">
          Your profile
        </a>
        <a class="dropdown-item" href="/lbsg323?tab=stars" data-ga-click="Header, go to starred repos, text:your stars">
          Your stars
        </a>
        <a class="dropdown-item" href="/explore" data-ga-click="Header, go to explore, text:explore">
          Explore
        </a>
          <a class="dropdown-item" href="/integrations" data-ga-click="Header, go to integrations, text:integrations">
            Integrations
          </a>
        <a class="dropdown-item" href="https://help.github.com" data-ga-click="Header, go to help, text:help">
          Help
        </a>

        <div class="dropdown-divider"></div>

        <a class="dropdown-item" href="/settings/profile" data-ga-click="Header, go to settings, icon:settings">
          Settings
        </a>

        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/logout" class="logout-form" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="KXmPv8d5OIaFojRVVGbPO5Lqp3LcZ1GsPwlA+/sJss1qYmSvR0ktmYx/2aN/kWuz8EGU4ARsU7khcNCUT05w5Q==" /></div>
          <button type="submit" class="dropdown-item dropdown-signout" data-ga-click="Header, sign out, icon:logout">
            Sign out
          </button>
</form>      </div>
    </div>
  </li>
</ul>


    
  </div>
</div>


      


    <div id="start-of-content" class="accessibility-aid"></div>

      <div id="js-flash-container">
</div>


    <div role="main">
        <div itemscope itemtype="http://schema.org/SoftwareSourceCode">
    <div id="js-repo-pjax-container" data-pjax-container>
      
<div class="pagehead repohead instapaper_ignore readability-menu experiment-repo-nav">
  <div class="container repohead-details-container">

    

<ul class="pagehead-actions">

  <li>
        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/notifications/subscribe" class="js-social-container" data-autosubmit="true" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="txq59cZKl5I2p67X1+V6OBraxFiuCkGt23DKF8MX4eb0AVLlRnqCjT96QyH8Et6weHH3ynYBQ7jFCVp4d1Ajzg==" /></div>      <input class="form-control" id="repository_id" name="repository_id" type="hidden" value="44729925" />

        <div class="select-menu js-menu-container js-select-menu">
          <a href="/bestcloud/ku8eye/subscription"
            class="btn btn-sm btn-with-count select-menu-button js-menu-target" role="button" tabindex="0" aria-haspopup="true"
            data-ga-click="Repository, click Watch settings, action:blob#show">
            <span class="js-select-button">
              <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
              Watch
            </span>
          </a>
          <a class="social-count js-social-count"
            href="/bestcloud/ku8eye/watchers"
            aria-label="56 users are watching this repository">
            56
          </a>

        <div class="select-menu-modal-holder">
          <div class="select-menu-modal subscription-menu-modal js-menu-content" aria-hidden="true">
            <div class="select-menu-header js-navigation-enable" tabindex="-1">
              <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
              <span class="select-menu-title">Notifications</span>
            </div>

              <div class="select-menu-list js-navigation-container" role="menu">

                <div class="select-menu-item js-navigation-item selected" role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input checked="checked" id="do_included" name="do" type="radio" value="included" />
                    <span class="select-menu-item-heading">Not watching</span>
                    <span class="description">Be notified when participating or @mentioned.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
                      Watch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input id="do_subscribed" name="do" type="radio" value="subscribed" />
                    <span class="select-menu-item-heading">Watching</span>
                    <span class="description">Be notified of all conversations.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
                      Unwatch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input id="do_ignore" name="do" type="radio" value="ignore" />
                    <span class="select-menu-item-heading">Ignoring</span>
                    <span class="description">Never be notified.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-mute" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8 2.81v10.38c0 .67-.81 1-1.28.53L3 10H1c-.55 0-1-.45-1-1V7c0-.55.45-1 1-1h2l3.72-3.72C7.19 1.81 8 2.14 8 2.81zm7.53 3.22l-1.06-1.06-1.97 1.97-1.97-1.97-1.06 1.06L11.44 8 9.47 9.97l1.06 1.06 1.97-1.97 1.97 1.97 1.06-1.06L13.56 8l1.97-1.97z"/></svg>
                      Stop ignoring
                    </span>
                  </div>
                </div>

              </div>

            </div>
          </div>
        </div>
</form>
  </li>

  <li>
    
  <div class="js-toggler-container js-social-container starring-container ">

    <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/bestcloud/ku8eye/unstar" class="starred" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="KcTLeKx6Lr/2EL9HReWwyhhtb/A3PFe2b4oK7Pg04Ilq3yBoLEo7oP/NUrFuEhRCesZcYu83VaNx85qDTHMioQ==" /></div>
      <button
        type="submit"
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Unstar this repository" title="Unstar bestcloud/ku8eye"
        data-ga-click="Repository, click unstar button, action:blob#show; text:Unstar">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 6l-4.9-.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14 7 11.67 11.33 14l-.93-4.74z"/></svg>
        Unstar
      </button>
        <a class="social-count js-social-count" href="/bestcloud/ku8eye/stargazers"
           aria-label="143 users starred this repository">
          143
        </a>
</form>
    <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/bestcloud/ku8eye/star" class="unstarred" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="SoaSv7wlw/6yj0mbbknxtNSoOf4Y/2iikqTkTGJrN5oJnXmvPBXW4btSpG1FvlU8tgMKbMD0areM3XQj1iz1sg==" /></div>
      <button
        type="submit"
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Star this repository" title="Star bestcloud/ku8eye"
        data-ga-click="Repository, click star button, action:blob#show; text:Star">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 6l-4.9-.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14 7 11.67 11.33 14l-.93-4.74z"/></svg>
        Star
      </button>
        <a class="social-count js-social-count" href="/bestcloud/ku8eye/stargazers"
           aria-label="143 users starred this repository">
          143
        </a>
</form>  </div>

  </li>

  <li>
          <a href="#fork-destination-box" class="btn btn-sm btn-with-count"
              title="Fork your own copy of bestcloud/ku8eye to your account"
              aria-label="Fork your own copy of bestcloud/ku8eye to your account"
              rel="facebox"
              data-ga-click="Repository, show fork modal, action:blob#show; text:Fork">
              <svg aria-hidden="true" class="octicon octicon-repo-forked" height="16" version="1.1" viewBox="0 0 10 16" width="10"><path fill-rule="evenodd" d="M8 1a1.993 1.993 0 0 0-1 3.72V6L5 8 3 6V4.72A1.993 1.993 0 0 0 2 1a1.993 1.993 0 0 0-1 3.72V6.5l3 3v1.78A1.993 1.993 0 0 0 5 15a1.993 1.993 0 0 0 1-3.72V9.5l3-3V4.72A1.993 1.993 0 0 0 8 1zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3 10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3-10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
            Fork
          </a>

          <div id="fork-destination-box" style="display: none;">
            <h2 class="facebox-header" data-facebox-id="facebox-header">Where should we fork this repository?</h2>
            <include-fragment src=""
                class="js-fork-select-fragment fork-select-fragment"
                data-url="/bestcloud/ku8eye/fork?fragment=1">
              <img alt="Loading" height="64" src="https://assets-cdn.github.com/images/spinners/octocat-spinner-128.gif" width="64" />
            </include-fragment>
          </div>

    <a href="/bestcloud/ku8eye/network" class="social-count"
       aria-label="128 users forked this repository">
      128
    </a>
  </li>
</ul>

    <h1 class="public ">
  <svg aria-hidden="true" class="octicon octicon-repo" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M4 9H3V8h1v1zm0-3H3v1h1V6zm0-2H3v1h1V4zm0-2H3v1h1V2zm8-1v12c0 .55-.45 1-1 1H6v2l-1.5-1.5L3 16v-2H1c-.55 0-1-.45-1-1V1c0-.55.45-1 1-1h10c.55 0 1 .45 1 1zm-1 10H1v2h2v-1h3v1h5v-2zm0-10H2v9h9V1z"/></svg>
  <span class="author" itemprop="author"><a href="/bestcloud" class="url fn" rel="author">bestcloud</a></span><!--
--><span class="path-divider">/</span><!--
--><strong itemprop="name"><a href="/bestcloud/ku8eye" data-pjax="#js-repo-pjax-container">ku8eye</a></strong>

</h1>

  </div>
  <div class="container">
    
<nav class="reponav js-repo-nav js-sidenav-container-pjax"
     itemscope
     itemtype="http://schema.org/BreadcrumbList"
     role="navigation"
     data-pjax="#js-repo-pjax-container">

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/bestcloud/ku8eye" aria-selected="true" class="js-selected-navigation-item selected reponav-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /bestcloud/ku8eye" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-code" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M9.5 3L8 4.5 11.5 8 8 11.5 9.5 13 14 8 9.5 3zm-5 0L0 8l4.5 5L6 11.5 2.5 8 6 4.5 4.5 3z"/></svg>
      <span itemprop="name">Code</span>
      <meta itemprop="position" content="1">
</a>  </span>

    <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
      <a href="/bestcloud/ku8eye/issues" class="js-selected-navigation-item reponav-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /bestcloud/ku8eye/issues" itemprop="url">
        <svg aria-hidden="true" class="octicon octicon-issue-opened" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"/></svg>
        <span itemprop="name">Issues</span>
        <span class="counter">5</span>
        <meta itemprop="position" content="2">
</a>    </span>

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/bestcloud/ku8eye/pulls" class="js-selected-navigation-item reponav-item" data-hotkey="g p" data-selected-links="repo_pulls /bestcloud/ku8eye/pulls" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-git-pull-request" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 11.28V5c-.03-.78-.34-1.47-.94-2.06C9.46 2.35 8.78 2.03 8 2H7V0L4 3l3 3V4h1c.27.02.48.11.69.31.21.2.3.42.31.69v6.28A1.993 1.993 0 0 0 10 15a1.993 1.993 0 0 0 1-3.72zm-1 2.92c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zM4 3c0-1.11-.89-2-2-2a1.993 1.993 0 0 0-1 3.72v6.56A1.993 1.993 0 0 0 2 15a1.993 1.993 0 0 0 1-3.72V4.72c.59-.34 1-.98 1-1.72zm-.8 10c0 .66-.55 1.2-1.2 1.2-.65 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
      <span itemprop="name">Pull requests</span>
      <span class="counter">2</span>
      <meta itemprop="position" content="3">
</a>  </span>

  <a href="/bestcloud/ku8eye/projects" class="js-selected-navigation-item reponav-item" data-selected-links="repo_projects new_repo_project repo_project /bestcloud/ku8eye/projects">
    <svg aria-hidden="true" class="octicon octicon-project" height="16" version="1.1" viewBox="0 0 15 16" width="15"><path fill-rule="evenodd" d="M10 12h3V2h-3v10zm-4-2h3V2H6v8zm-4 4h3V2H2v12zm-1 1h13V1H1v14zM14 0H1a1 1 0 0 0-1 1v14a1 1 0 0 0 1 1h13a1 1 0 0 0 1-1V1a1 1 0 0 0-1-1z"/></svg>
    Projects
    <span class="counter">0</span>
</a>
    <a href="/bestcloud/ku8eye/wiki" class="js-selected-navigation-item reponav-item" data-hotkey="g w" data-selected-links="repo_wiki /bestcloud/ku8eye/wiki">
      <svg aria-hidden="true" class="octicon octicon-book" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M3 5h4v1H3V5zm0 3h4V7H3v1zm0 2h4V9H3v1zm11-5h-4v1h4V5zm0 2h-4v1h4V7zm0 2h-4v1h4V9zm2-6v9c0 .55-.45 1-1 1H9.5l-1 1-1-1H2c-.55 0-1-.45-1-1V3c0-.55.45-1 1-1h5.5l1 1 1-1H15c.55 0 1 .45 1 1zm-8 .5L7.5 3H2v9h6V3.5zm7-.5H9.5l-.5.5V12h6V3z"/></svg>
      Wiki
</a>

  <a href="/bestcloud/ku8eye/pulse" class="js-selected-navigation-item reponav-item" data-selected-links="pulse /bestcloud/ku8eye/pulse">
    <svg aria-hidden="true" class="octicon octicon-pulse" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M11.5 8L8.8 5.4 6.6 8.5 5.5 1.6 2.38 8H0v2h3.6l.9-1.8.9 5.4L9 8.5l1.6 1.5H14V8z"/></svg>
    Pulse
</a>
  <a href="/bestcloud/ku8eye/graphs" class="js-selected-navigation-item reponav-item" data-selected-links="repo_graphs repo_contributors /bestcloud/ku8eye/graphs">
    <svg aria-hidden="true" class="octicon octicon-graph" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M16 14v1H0V0h1v14h15zM5 13H3V8h2v5zm4 0H7V3h2v10zm4 0h-2V6h2v7z"/></svg>
    Graphs
</a>

</nav>

  </div>
</div>

<div class="container new-discussion-timeline experiment-repo-nav">
  <div class="repository-content">

    

<a href="/bestcloud/ku8eye/blob/e8a029c86b1d961653a5a707f1e807fb3c1cf396/github-develop-guide.md" class="d-none js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:54d49ece89c0c2b88f3e604c388e9679 -->

<div class="file-navigation js-zeroclipboard-container">
  
<div class="select-menu branch-select-menu js-menu-container js-select-menu float-left">
  <button class="btn btn-sm select-menu-button js-menu-target css-truncate" data-hotkey="w"
    
    type="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
    <i>Branch:</i>
    <span class="js-select-button css-truncate-target">master</span>
  </button>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax aria-hidden="true">

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
        <span class="select-menu-title">Switch branches/tags</span>
      </div>

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Filter branches/tags" id="context-commitish-filter-field" class="form-control js-filterable-field js-navigation-enable" placeholder="Filter branches/tags">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" data-filter-placeholder="Filter branches/tags" class="js-select-menu-tab" role="tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" data-filter-placeholder="Find a tag…" class="js-select-menu-tab" role="tab">Tags</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches" role="menu">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/bestcloud/ku8eye/blob/Member-Profile/github-develop-guide.md"
               data-name="Member-Profile"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                Member-Profile
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open selected"
               href="/bestcloud/ku8eye/blob/master/github-develop-guide.md"
               data-name="master"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                master
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/bestcloud/ku8eye/blob/revert-1-master/github-develop-guide.md"
               data-name="revert-1-master"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                revert-1-master
              </span>
            </a>
        </div>

          <div class="select-menu-no-results">Nothing to show</div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div>

    </div>
  </div>
</div>

  <div class="BtnGroup float-right">
    <a href="/bestcloud/ku8eye/find/master"
          class="js-pjax-capture-input btn btn-sm BtnGroup-item"
          data-pjax
          data-hotkey="t">
      Find file
    </a>
    <button aria-label="Copy file path to clipboard" class="js-zeroclipboard btn btn-sm BtnGroup-item tooltipped tooltipped-s" data-copied-hint="Copied!" type="button">Copy path</button>
  </div>
  <div class="breadcrumb js-zeroclipboard-target">
    <span class="repo-root js-repo-root"><span class="js-path-segment"><a href="/bestcloud/ku8eye"><span>ku8eye</span></a></span></span><span class="separator">/</span><strong class="final-path">github-develop-guide.md</strong>
  </div>
</div>


  <div class="commit-tease">
      <span class="float-right">
        <a class="commit-tease-sha" href="/bestcloud/ku8eye/commit/bb58794d916018b8737d2056efdcf701e053a978" data-pjax>
          bb58794
        </a>
        <relative-time datetime="2015-11-13T03:59:08Z">Nov 13, 2015</relative-time>
      </span>
      <div>
        <img alt="@leader-us" class="avatar" height="20" src="https://avatars3.githubusercontent.com/u/5953348?v=3&amp;s=40" width="20" />
        <a href="/leader-us" class="user-mention" rel="contributor">leader-us</a>
          <a href="/bestcloud/ku8eye/commit/bb58794d916018b8737d2056efdcf701e053a978" class="message" data-pjax="true" title="Update github-develop-guide.md">Update github-develop-guide.md</a>
      </div>

    <div class="commit-tease-contributors">
      <button type="button" class="btn-link muted-link contributors-toggle" data-facebox="#blob_contributors_box">
        <strong>3</strong>
         contributors
      </button>
          <a class="avatar-link tooltipped tooltipped-s" aria-label="leader-us" href="/bestcloud/ku8eye/commits/master/github-develop-guide.md?author=leader-us"><img alt="@leader-us" class="avatar" height="20" src="https://avatars3.githubusercontent.com/u/5953348?v=3&amp;s=40" width="20" /> </a>
    <a class="avatar-link tooltipped tooltipped-s" aria-label="KinglyWayne" href="/bestcloud/ku8eye/commits/master/github-develop-guide.md?author=KinglyWayne"><img alt="@KinglyWayne" class="avatar" height="20" src="https://avatars3.githubusercontent.com/u/3232817?v=3&amp;s=40" width="20" /> </a>
    <a class="avatar-link tooltipped tooltipped-s" aria-label="gongzh" href="/bestcloud/ku8eye/commits/master/github-develop-guide.md?author=gongzh"><img alt="@gongzh" class="avatar" height="20" src="https://avatars2.githubusercontent.com/u/15317040?v=3&amp;s=40" width="20" /> </a>


    </div>

    <div id="blob_contributors_box" style="display:none">
      <h2 class="facebox-header" data-facebox-id="facebox-header">Users who have contributed to this file</h2>
      <ul class="facebox-user-list" data-facebox-id="facebox-description">
          <li class="facebox-user-list-item">
            <img alt="@leader-us" height="24" src="https://avatars1.githubusercontent.com/u/5953348?v=3&amp;s=48" width="24" />
            <a href="/leader-us">leader-us</a>
          </li>
          <li class="facebox-user-list-item">
            <img alt="@KinglyWayne" height="24" src="https://avatars1.githubusercontent.com/u/3232817?v=3&amp;s=48" width="24" />
            <a href="/KinglyWayne">KinglyWayne</a>
          </li>
          <li class="facebox-user-list-item">
            <img alt="@gongzh" height="24" src="https://avatars0.githubusercontent.com/u/15317040?v=3&amp;s=48" width="24" />
            <a href="/gongzh">gongzh</a>
          </li>
      </ul>
    </div>
  </div>


<div class="file">
  <div class="file-header">
  <div class="file-actions">

    <div class="BtnGroup">
      <a href="/bestcloud/ku8eye/raw/master/github-develop-guide.md" class="btn btn-sm BtnGroup-item" id="raw-url">Raw</a>
        <a href="/bestcloud/ku8eye/blame/master/github-develop-guide.md" class="btn btn-sm js-update-url-with-hash BtnGroup-item">Blame</a>
      <a href="/bestcloud/ku8eye/commits/master/github-develop-guide.md" class="btn btn-sm BtnGroup-item" rel="nofollow">History</a>
    </div>

        <a class="btn-octicon tooltipped tooltipped-nw"
           href="github-windows://openRepo/https://github.com/bestcloud/ku8eye?branch=master&amp;filepath=github-develop-guide.md"
           aria-label="Open this file in GitHub Desktop"
           data-ga-click="Repository, open with desktop, type:windows">
            <svg aria-hidden="true" class="octicon octicon-device-desktop" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M15 2H1c-.55 0-1 .45-1 1v9c0 .55.45 1 1 1h5.34c-.25.61-.86 1.39-2.34 2h8c-1.48-.61-2.09-1.39-2.34-2H15c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm0 9H1V3h14v8z"/></svg>
        </a>

        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/bestcloud/ku8eye/edit/master/github-develop-guide.md" class="inline-form js-update-url-with-hash" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="C3Lnpw+NVH5e+9VwrpldrSLVOpfCHLZh9jWaxw8MbXRIaQy3j71BYVcmOIaFbvklQH4JBRoXtHToTAqou0uvXA==" /></div>
          <button class="btn-octicon tooltipped tooltipped-nw" type="submit"
            aria-label="Edit the file in your fork of this project" data-hotkey="e" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-pencil" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M0 12v3h3l8-8-3-3-8 8zm3 2H1v-2h1v1h1v1zm10.3-9.3L12 6 9 3l1.3-1.3a.996.996 0 0 1 1.41 0l1.59 1.59c.39.39.39 1.02 0 1.41z"/></svg>
          </button>
</form>        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/bestcloud/ku8eye/delete/master/github-develop-guide.md" class="inline-form" data-form-nonce="d3e0f4988d6670ed600b2c2fe299ec47b82fec6c" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="9c9U5DSwTdWww4tZK4ugfVafcFedMttZi8b6vgSFz8W21L/0tIBYyrkeZq8AfAT1NDRDxUU52UyVv2rRsMIN7Q==" /></div>
          <button class="btn-octicon btn-octicon-danger tooltipped tooltipped-nw" type="submit"
            aria-label="Delete the file in your fork of this project" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-trashcan" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 2H9c0-.55-.45-1-1-1H5c-.55 0-1 .45-1 1H2c-.55 0-1 .45-1 1v1c0 .55.45 1 1 1v9c0 .55.45 1 1 1h7c.55 0 1-.45 1-1V5c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm-1 12H3V5h1v8h1V5h1v8h1V5h1v8h1V5h1v9zm1-10H2V3h9v1z"/></svg>
          </button>
</form>  </div>

  <div class="file-info">
      154 lines (106 sloc)
      <span class="file-info-divider"></span>
    5.11 KB
  </div>
</div>

  
  <div id="readme" class="readme blob instapaper_body">
    <article class="markdown-body entry-content" itemprop="text"><p><a href="/bestcloud/ku8eye/blob/master/res/ku8eye.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/ku8eye.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p>

<p>Eclipse中导入maven工程时候，如果公司是代理上网，则需要设置M2插件的代理，这个是在Ecliplse之外的设置，如果是win7系统，则通常在C:\Users\youaccount\.m2 目录下是m2的repository，目录地下有repository目录.
在.m2目录下建立settings.xml文档，里面设置代理，内容如下</p>

<div class="highlight highlight-text-xml"><pre>&lt;<span class="pl-ent">settings</span>&gt;  
  &lt;<span class="pl-ent">proxies</span>&gt;  
    &lt;<span class="pl-ent">proxy</span>&gt;  
      &lt;<span class="pl-ent">id</span>&gt;my-proxy&lt;/<span class="pl-ent">id</span>&gt;  
      &lt;<span class="pl-ent">active</span>&gt;true&lt;/<span class="pl-ent">active</span>&gt;  
      &lt;<span class="pl-ent">protocol</span>&gt;http&lt;/<span class="pl-ent">protocol</span>&gt;  
      &lt;<span class="pl-ent">host</span>&gt;proxy.xxxx.com&lt;/<span class="pl-ent">host</span>&gt;  
      &lt;<span class="pl-ent">port</span>&gt;8080&lt;/<span class="pl-ent">port</span>&gt;  
    &lt;/<span class="pl-ent">proxy</span>&gt;  
    &lt;<span class="pl-ent">proxy</span>&gt;  
      &lt;<span class="pl-ent">id</span>&gt;my-proxy2&lt;/<span class="pl-ent">id</span>&gt;  
      &lt;<span class="pl-ent">active</span>&gt;true&lt;/<span class="pl-ent">active</span>&gt;  
      &lt;<span class="pl-ent">protocol</span>&gt;https&lt;/<span class="pl-ent">protocol</span>&gt;  
      &lt;<span class="pl-ent">host</span>&gt;proxy.xxxx.com&lt;/<span class="pl-ent">host</span>&gt;  
      &lt;<span class="pl-ent">port</span>&gt;8080&lt;/<span class="pl-ent">port</span>&gt;  
    &lt;/<span class="pl-ent">proxy</span>&gt;  
  &lt;/<span class="pl-ent">proxies</span>&gt;  
&lt;/<span class="pl-ent">settings</span>&gt;  </pre></div>

<p>如果是独立的maven，则配置文件应该是在maven的安装目录的conf目录下的setting.xml文件</p>

<div class="highlight highlight-text-xml"><pre>&lt;<span class="pl-ent">pluginGroups</span>&gt;
&lt;/<span class="pl-ent">pluginGroups</span>&gt;
&lt;<span class="pl-ent">proxies</span>&gt;
&lt;<span class="pl-ent">proxy</span>&gt;
&lt;<span class="pl-ent">id</span>&gt;my-proxy&lt;/<span class="pl-ent">id</span>&gt;
&lt;<span class="pl-ent">active</span>&gt;true&lt;/<span class="pl-ent">active</span>&gt;
&lt;<span class="pl-ent">protocol</span>&gt;http&lt;/<span class="pl-ent">protocol</span>&gt;
&lt;<span class="pl-ent">host</span>&gt;xxxx&lt;/<span class="pl-ent">host</span>&gt;
&lt;<span class="pl-ent">port</span>&gt;8080&lt;/<span class="pl-ent">port</span>&gt;
&lt;/<span class="pl-ent">proxy</span>&gt;
&lt;<span class="pl-ent">proxy</span>&gt;
&lt;<span class="pl-ent">id</span>&gt;my-proxy2&lt;/<span class="pl-ent">id</span>&gt;
&lt;<span class="pl-ent">active</span>&gt;true&lt;/<span class="pl-ent">active</span>&gt;
&lt;<span class="pl-ent">protocol</span>&gt;https&lt;/<span class="pl-ent">protocol</span>&gt;
&lt;<span class="pl-ent">host</span>&gt;xxxx&lt;/<span class="pl-ent">host</span>&gt;
&lt;<span class="pl-ent">port</span>&gt;8080&lt;/<span class="pl-ent">port</span>&gt;
&lt;/<span class="pl-ent">proxy</span>&gt;
&lt;/<span class="pl-ent">proxies</span>&gt;</pre></div>

<h1><a id="user-content-一fork项目到自己的github账号" class="anchor" href="#一fork项目到自己的github账号" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>一：fork项目到自己的github账号</h1>

<ol>
<li>点击fork到自己的账号下
<a href="/bestcloud/ku8eye/blob/master/res/1.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/1.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
<li>查看自己fork项目地址，用户名假如my，则自己的项目地址：<a href="https://github.com/my/ku8eye">https://github.com/my/ku8eye</a></li>
</ol>

<h1><a id="user-content-二安装客户端工具" class="anchor" href="#二安装客户端工具" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>二：安装客户端工具</h1>

<ul>
<li>Git-2.6.2-64-bit.exe</li>
<li>TotoiseGit</li>
</ul>

<h1><a id="user-content-三检出项目" class="anchor" href="#三检出项目" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>三：检出项目</h1>

<ol>
<li><p>如果需要代理上网，则右键菜单TotoiseGit——》Settings——》Network设置代理
<a href="/bestcloud/ku8eye/blob/master/res/2.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/2.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>文件右键菜单：Git Clone
<a href="/bestcloud/ku8eye/blob/master/res/3.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/3.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>填入自己Fork的地址，注意不是官方的项目地址
<a href="/bestcloud/ku8eye/blob/master/res/4.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/4.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>增加官方源地址:upstream</p></li>
<li><p>检出的项目的邮件菜单TotoiseGit——》Settings
<a href="/bestcloud/ku8eye/blob/master/res/5.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/5.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>输入upstream的地址，注意这里是官方的项目地址，点击保存。原来的origin则是你Fork的地址，保持不变。
<a href="/bestcloud/ku8eye/blob/master/res/6.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/6.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>保存的时候，第一次会提升，防止出错，关闭默认从该地址pull/fetch的功能，我们要点击"No"，即默认从官方源pull/fetch，以同步到我们的本地库中。
<a href="/bestcloud/ku8eye/blob/master/res/7.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/7.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>接下来会从upstream拉取一次源码：
<a href="/bestcloud/ku8eye/blob/master/res/8.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/8.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/9.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/9.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>效果如下：
<a href="/bestcloud/ku8eye/blob/master/res/10.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/10.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/11.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/11.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
</ol>

<h1><a id="user-content-四提交代码" class="anchor" href="#四提交代码" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>四：提交代码</h1>

<p>如果我们本地有代码改变，比如新增文件或代码，流程如下：</p>

<p>Commit（本地提交）-&gt;push（提交到自己Fork的地址）-&gt;发起pull request(可以工具里或者gihub自己的项目主页里)</p>

<ol>
<li><p>比如我们新增了一个文件，右键TotoiseGit –》Add，添加到版本控制文件列表中，然后Commit到本地库里。
<a href="/bestcloud/ku8eye/blob/master/res/12.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/12.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/13.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/13.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/14.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/14.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/15.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/15.png" alt="ImageLoadFailed" style="max-width:100%;"></a></p></li>
<li><p>Commit完成以后可以继续Push，或者以后Push，只有Push到自己Fork地址以后，才是提交到github里了，否则只是本地保存了。
输入自己的用户名密码提交
<a href="/bestcloud/ku8eye/blob/master/res/16.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/16.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/17.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/17.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
注意到，目标地址Remoet为Origin地址，即我们自己Fork的地址。</p></li>
<li>Push成功以后，显示如下内容，可以继续发起pull reqest请求，将变更提交到官方源地址(upstream)，请求合并你的变更：
<a href="/bestcloud/ku8eye/blob/master/res/18.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/18.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
一般是在github网站上发起pull request的，用自己的账号登录github，查看项目变动情况，可以看到自己账号的代码分支比官方源多一个Commit。点击Pull request请求官方合并你的Commit。
<a href="/bestcloud/ku8eye/blob/master/res/19.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/19.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/20.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/20.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
发起Pull Request的过程中，可以看到你提交的文件数量，具体文件，以及提交时候的注释内容，有助于官方来确定你的提交是否能被接纳。</li>
</ol>

<h1><a id="user-content-五官方merge请求" class="anchor" href="#五官方merge请求" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>五：官方Merge请求</h1>

<ol>
<li>所有的Pull request会在官方项目信息里看到
<a href="/bestcloud/ku8eye/blob/master/res/21.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/21.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
<li>如果没有冲突，则官方会选择合并，
<a href="/bestcloud/ku8eye/blob/master/res/22.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/22.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
<li>也可能会拒绝，拒绝的话，组好提供详细的拒绝理由，供请求者参考和改进，重新发起Pull。</li>
<li>如果无法自动合并，则表明冲突了，需要发起者同步一次官网最新代码并且Merge以后再提交。</li>
</ol>

<h1><a id="user-content-六从官网源同步代码" class="anchor" href="#六从官网源同步代码" aria-hidden="true"><svg aria-hidden="true" class="octicon octicon-link" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>六：从官网源同步代码</h1>

<p>可以用pull或者fetch两种方式从官网源（upstream）同步代码，fetch是执行了pull +merge的操作，即pull下来与本地的仓库进行merge操作，建议百度更详细的说明。</p>

<ol>
<li>在自己fork的项目下，可以看到当前版本与官网源的差距，如下图所示：
<a href="/bestcloud/ku8eye/blob/master/res/23.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/23.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
<li>此次，是可以从官方源fetch/pull最新代码的，如下图所示，Remote选择upstream（官方源）：
<a href="/bestcloud/ku8eye/blob/master/res/24.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/24.png" alt="ImageLoadFailed" style="max-width:100%;"></a>
<a href="/bestcloud/ku8eye/blob/master/res/25.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/25.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
<li>可以点击Pulled Diff来查看具体的变化信息：
<a href="/bestcloud/ku8eye/blob/master/res/26.png" target="_blank"><img src="/bestcloud/ku8eye/raw/master/res/26.png" alt="ImageLoadFailed" style="max-width:100%;"></a></li>
</ol>
</article>
  </div>

</div>

<button type="button" data-facebox="#jump-to-line" data-facebox-class="linejump" data-hotkey="l" class="d-none">Jump to Line</button>
<div id="jump-to-line" style="display:none">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="" class="js-jump-to-line-form" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <input class="form-control linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" aria-label="Jump to line" autofocus>
    <button type="submit" class="btn">Go</button>
</form></div>

  </div>
  <div class="modal-backdrop js-touch-events"></div>
</div>


    </div>
  </div>

    </div>

        <div class="container site-footer-container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links float-right">
        <li><a href="https://github.com/contact" data-ga-click="Footer, go to contact, text:contact">Contact GitHub</a></li>
      <li><a href="https://developer.github.com" data-ga-click="Footer, go to api, text:api">API</a></li>
      <li><a href="https://training.github.com" data-ga-click="Footer, go to training, text:training">Training</a></li>
      <li><a href="https://shop.github.com" data-ga-click="Footer, go to shop, text:shop">Shop</a></li>
        <li><a href="https://github.com/blog" data-ga-click="Footer, go to blog, text:blog">Blog</a></li>
        <li><a href="https://github.com/about" data-ga-click="Footer, go to about, text:about">About</a></li>

    </ul>

    <a href="https://github.com" aria-label="Homepage" class="site-footer-mark" title="GitHub">
      <svg aria-hidden="true" class="octicon octicon-mark-github" height="24" version="1.1" viewBox="0 0 16 16" width="24"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
</a>
    <ul class="site-footer-links">
      <li>&copy; 2016 <span title="0.08705s from github-fe125-cp1-prd.iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="https://github.com/site/terms" data-ga-click="Footer, go to terms, text:terms">Terms</a></li>
        <li><a href="https://github.com/site/privacy" data-ga-click="Footer, go to privacy, text:privacy">Privacy</a></li>
        <li><a href="https://github.com/security" data-ga-click="Footer, go to security, text:security">Security</a></li>
        <li><a href="https://status.github.com/" data-ga-click="Footer, go to status, text:status">Status</a></li>
        <li><a href="https://help.github.com" data-ga-click="Footer, go to help, text:help">Help</a></li>
    </ul>
  </div>
</div>



    

    <div id="ajax-error-message" class="ajax-error-message flash flash-error">
      <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
      <button type="button" class="flash-close js-flash-close js-ajax-error-dismiss" aria-label="Dismiss error">
        <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
      </button>
      You can't perform that action at this time.
    </div>


      
      <script crossorigin="anonymous" integrity="sha256-q1D7/rLAuHSPor/tXQvMz8BrgKvykFhqfRxvI5AvXfM=" src="https://assets-cdn.github.com/assets/frameworks-ab50fbfeb2c0b8748fa2bfed5d0bcccfc06b80abf290586a7d1c6f23902f5df3.js"></script>
      <script async="async" crossorigin="anonymous" integrity="sha256-q1O55iRgoevO9Kq555Y7W9JJdHRoaWTSfkznVqZfp+A=" src="https://assets-cdn.github.com/assets/github-ab53b9e62460a1ebcef4aab9e7963b5bd2497474686964d27e4ce756a65fa7e0.js"></script>
      
      
      
      
    <div class="js-stale-session-flash stale-session-flash flash flash-warn flash-banner d-none">
      <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
      <span class="signed-in-tab-flash">You signed in with another tab or window. <a href="">Reload</a> to refresh your session.</span>
      <span class="signed-out-tab-flash">You signed out in another tab or window. <a href="">Reload</a> to refresh your session.</span>
    </div>
    <div class="facebox" id="facebox" style="display:none;">
  <div class="facebox-popup">
    <div class="facebox-content" role="dialog" aria-labelledby="facebox-header" aria-describedby="facebox-description">
    </div>
    <button type="button" class="facebox-close js-facebox-close" aria-label="Close modal">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
    </button>
  </div>
</div>

  </body>
</html>

