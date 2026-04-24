import { useState } from "react";

// ─── DESIGN SYSTEM ──────────────────────────────────────────
const COLORS = {
  gold: "#E1A500",
  goldLight: "#F0C030",
  goldDark: "#C89000",
  goldSubtle: "rgba(225,165,0,0.12)",
  goldBorder: "rgba(225,165,0,0.3)",
  bg: "#121212",
  surface: "#1E1E1E",
  surfaceContainer: "#2A2A2A",
  text: "#E8E2D6",
  textDim: "#9E9A92",
  error: "#FFB4AB",
  errorBg: "rgba(255,180,171,0.1)",
  outline: "#3A3A3A",
  success: "#4ADE80",
  successBg: "rgba(74,222,128,0.12)",
  info: "#60A5FA",
  infoBg: "rgba(96,165,250,0.12)",
  white: "#FFFFFF",
};

const ACTIVITY_TYPES = [
  { icon: "🏃", label: "Run", color: COLORS.gold },
  { icon: "🚴", label: "Cycle", color: COLORS.info },
  { icon: "🚶", label: "Walk", color: COLORS.success },
  { icon: "🥾", label: "Hike", color: "#F97316" },
  { icon: "⚡", label: "Other", color: COLORS.textDim },
];

// ─── TRAKKR APP ICON ────────────────────────────────────────
const TrakkrIcon = ({ size = 120 }) => (
  <svg viewBox="0 0 512 512" width={size} height={size}>
    <defs>
      <linearGradient id="tgi" x1="0%" y1="0%" x2="100%" y2="100%">
        <stop offset="0%" stopColor="#F0C030"/>
        <stop offset="50%" stopColor="#E1A500"/>
        <stop offset="100%" stopColor="#C89000"/>
      </linearGradient>
    </defs>
    <rect width="512" height="512" rx="108" fill="#121212"/>
    {/* Trail/path motif */}
    <path d="M140 380 Q180 300 220 310 Q260 320 280 260 Q300 200 340 220 Q380 240 380 160"
      fill="none" stroke="url(#tgi)" strokeWidth="28" strokeLinecap="round" strokeLinejoin="round"/>
    {/* Location pin at end */}
    <circle cx="380" cy="148" r="24" fill="url(#tgi)"/>
    <circle cx="380" cy="142" r="10" fill="#121212"/>
    {/* Pulse rings */}
    <circle cx="380" cy="148" r="36" fill="none" stroke="url(#tgi)" strokeWidth="3" opacity="0.4"/>
    <circle cx="380" cy="148" r="50" fill="none" stroke="url(#tgi)" strokeWidth="2" opacity="0.2"/>
    <text x="256" y="440" fontFamily="Arial,sans-serif" fontSize="52" fontWeight="800" fill="url(#tgi)" textAnchor="middle" letterSpacing="10">TRAKKR</text>
  </svg>
);

// ─── TAB ICONS ──────────────────────────────────────────────
const TabIcons = {
  track: (s, active) => (
    <svg viewBox="0 0 24 24" width={s} height={s} fill="none" stroke={active ? COLORS.gold : COLORS.textDim} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="3"/><path d="M12 2v4"/><path d="M12 18v4"/><path d="M2 12h4"/><path d="M18 12h4"/>
    </svg>
  ),
  history: (s, active) => (
    <svg viewBox="0 0 24 24" width={s} height={s} fill="none" stroke={active ? COLORS.gold : COLORS.textDim} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10"/><polyline points="12,6 12,12 16,14"/>
    </svg>
  ),
  routes: (s, active) => (
    <svg viewBox="0 0 24 24" width={s} height={s} fill="none" stroke={active ? COLORS.gold : COLORS.textDim} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M3 17l4-4 4 4 4-4 4 4"/><circle cx="7" cy="7" r="3"/><path d="M20 7h-6"/>
    </svg>
  ),
  profile: (s, active) => (
    <svg viewBox="0 0 24 24" width={s} height={s} fill="none" stroke={active ? COLORS.gold : COLORS.textDim} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 4-7 8-7s8 3 8 7"/>
    </svg>
  ),
};

// ─── REUSABLE COMPONENTS ────────────────────────────────────
const PhoneMockup = ({ children, title, activeNav = "track", showNav = true }) => (
  <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 8, flexShrink: 0 }}>
    {title && <div style={{ color: COLORS.gold, fontWeight: 700, fontSize: 12, letterSpacing: 1.5, textTransform: "uppercase", marginBottom: 4, textAlign: "center", maxWidth: 280 }}>{title}</div>}
    <div style={{
      width: 280, height: 580, background: COLORS.bg, borderRadius: 28,
      border: `2px solid ${COLORS.outline}`, overflow: "hidden", position: "relative",
      boxShadow: "0 8px 32px rgba(0,0,0,0.6)",
    }}>
      {/* Status bar */}
      <div style={{ height: 28, background: COLORS.bg, display: "flex", alignItems: "center", justifyContent: "space-between", padding: "0 16px", fontSize: 10, color: COLORS.textDim }}>
        <span>12:30</span><span>⚡ 85%</span>
      </div>
      {/* Content */}
      <div style={{ height: showNav ? 496 : 552, overflowY: "auto", overflowX: "hidden" }}>{children}</div>
      {/* Bottom navigation */}
      {showNav && (
        <div style={{
          height: 56, background: COLORS.surface, display: "flex", alignItems: "center",
          justifyContent: "space-around", borderTop: `1px solid ${COLORS.outline}`, padding: "0 4px",
        }}>
          {[
            { icon: TabIcons.track, label: "Track", id: "track" },
            { icon: TabIcons.history, label: "History", id: "history" },
            { icon: TabIcons.routes, label: "Routes", id: "routes" },
            { icon: TabIcons.profile, label: "Profile", id: "profile" },
          ].map(t => (
            <div key={t.id} style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 2 }}>
              {t.icon(20, activeNav === t.id)}
              <span style={{ fontSize: 9, color: activeNav === t.id ? COLORS.gold : COLORS.textDim, fontWeight: activeNav === t.id ? 700 : 400 }}>{t.label}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  </div>
);

const AppBar = ({ title, leftIcon, rightIcons, subtitle }) => (
  <div style={{ height: subtitle ? 56 : 48, display: "flex", alignItems: "center", padding: "0 12px", gap: 8 }}>
    {leftIcon && <span style={{ color: COLORS.textDim, fontSize: 18, cursor: "pointer" }}>{leftIcon}</span>}
    <div style={{ flex: 1 }}>
      <span style={{ color: COLORS.text, fontWeight: 700, fontSize: 17, letterSpacing: 0.3 }}>{title}</span>
      {subtitle && <div style={{ fontSize: 10, color: COLORS.textDim, marginTop: 1 }}>{subtitle}</div>}
    </div>
    {rightIcons && <div style={{ display: "flex", gap: 12, color: COLORS.textDim, fontSize: 16 }}>{rightIcons}</div>}
  </div>
);

const Card = ({ children, highlight, style: s }) => (
  <div style={{
    background: highlight ? COLORS.goldSubtle : COLORS.surface,
    borderRadius: 14, padding: 14,
    border: `1px solid ${highlight ? COLORS.goldBorder : COLORS.outline}`,
    ...s,
  }}>{children}</div>
);

const Chip = ({ label, active, color }) => (
  <div style={{
    padding: "6px 14px", borderRadius: 20, fontSize: 11, fontWeight: 600,
    background: active ? (color || COLORS.gold) : COLORS.surfaceContainer,
    color: active ? COLORS.bg : COLORS.textDim,
    border: `1px solid ${active ? "transparent" : COLORS.outline}`,
    whiteSpace: "nowrap",
  }}>{label}</div>
);

const StatBox = ({ label, value, unit, large }) => (
  <div style={{ textAlign: "center", flex: 1 }}>
    <div style={{ fontSize: large ? 28 : 20, fontWeight: 800, color: COLORS.text, fontFamily: "monospace", letterSpacing: -0.5 }}>{value}</div>
    {unit && <div style={{ fontSize: 9, color: COLORS.gold, fontWeight: 600, marginTop: -2 }}>{unit}</div>}
    <div style={{ fontSize: 9, color: COLORS.textDim, marginTop: 2, letterSpacing: 0.5 }}>{label}</div>
  </div>
);

const SectionLabel = ({ children }) => (
  <div style={{ fontSize: 9, color: COLORS.gold, fontWeight: 700, letterSpacing: 1.2, marginBottom: 6, padding: "0 2px" }}>{children}</div>
);

const ListItem = ({ icon, title, subtitle, right, border = true }) => (
  <div style={{
    display: "flex", alignItems: "center", gap: 10, padding: "10px 4px",
    borderBottom: border ? `1px solid ${COLORS.outline}` : "none",
  }}>
    {icon && <span style={{ fontSize: 16, width: 24, textAlign: "center" }}>{icon}</span>}
    <div style={{ flex: 1, minWidth: 0 }}>
      <div style={{ fontSize: 13, color: COLORS.text, fontWeight: 500, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{title}</div>
      {subtitle && <div style={{ fontSize: 10, color: COLORS.textDim, marginTop: 1 }}>{subtitle}</div>}
    </div>
    {right && <div style={{ fontSize: 11, color: COLORS.textDim, flexShrink: 0 }}>{right}</div>}
  </div>
);

const PrimaryButton = ({ children, icon }) => (
  <div style={{
    background: COLORS.gold, borderRadius: 14, padding: "14px 0", textAlign: "center",
    color: COLORS.bg, fontWeight: 700, fontSize: 15, letterSpacing: 0.5,
    display: "flex", alignItems: "center", justifyContent: "center", gap: 8,
  }}>{icon}{children}</div>
);

const OutlineButton = ({ children, color }) => (
  <div style={{
    borderRadius: 12, padding: "11px 0", textAlign: "center",
    border: `1.5px solid ${color || COLORS.gold}`,
    color: color || COLORS.gold, fontWeight: 600, fontSize: 13,
  }}>{children}</div>
);

const GhostButton = ({ children, color }) => (
  <div style={{
    borderRadius: 12, padding: "11px 0", textAlign: "center",
    background: COLORS.surfaceContainer, color: color || COLORS.text, fontSize: 13,
  }}>{children}</div>
);

// Fake map component
const MapPlaceholder = ({ height = 180, route = true, pin = false, live = false }) => (
  <div style={{
    height, background: "#1a2a1a", position: "relative", overflow: "hidden",
  }}>
    {/* Grid lines for map feel */}
    {[...Array(8)].map((_, i) => (
      <div key={`h${i}`} style={{ position: "absolute", top: `${(i + 1) * 12.5}%`, left: 0, right: 0, height: 1, background: "rgba(74,222,128,0.06)" }}/>
    ))}
    {[...Array(6)].map((_, i) => (
      <div key={`v${i}`} style={{ position: "absolute", left: `${(i + 1) * 16.6}%`, top: 0, bottom: 0, width: 1, background: "rgba(74,222,128,0.06)" }}/>
    ))}
    {/* Route line */}
    {route && (
      <svg style={{ position: "absolute", inset: 0 }} viewBox="0 0 280 180" preserveAspectRatio="none">
        <path d="M40 140 Q80 100 100 110 Q130 125 160 80 Q190 35 230 60 Q250 72 250 50"
          fill="none" stroke={COLORS.gold} strokeWidth="3" strokeLinecap="round" opacity="0.9"/>
        {live && <circle cx="250" cy="50" r="5" fill={COLORS.gold}/>}
        {live && <circle cx="250" cy="50" r="10" fill="none" stroke={COLORS.gold} strokeWidth="1.5" opacity="0.5"/>}
      </svg>
    )}
    {pin && (
      <div style={{ position: "absolute", top: "40%", left: "55%", transform: "translate(-50%, -100%)" }}>
        <div style={{ width: 16, height: 16, background: COLORS.gold, borderRadius: "50% 50% 50% 0", transform: "rotate(-45deg)", border: "2px solid #121212" }}/>
      </div>
    )}
    {/* Map controls */}
    <div style={{ position: "absolute", right: 8, top: 8, display: "flex", flexDirection: "column", gap: 4 }}>
      <div style={{ width: 28, height: 28, background: "rgba(30,30,30,0.85)", borderRadius: 6, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 14, color: COLORS.textDim }}>+</div>
      <div style={{ width: 28, height: 28, background: "rgba(30,30,30,0.85)", borderRadius: 6, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 14, color: COLORS.textDim }}>−</div>
    </div>
    {/* Center button */}
    <div style={{ position: "absolute", right: 8, bottom: 8 }}>
      <div style={{ width: 28, height: 28, background: "rgba(30,30,30,0.85)", borderRadius: 6, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 12, color: COLORS.gold }}>◎</div>
    </div>
  </div>
);

// ─── ONBOARDING SCREENS ─────────────────────────────────────

const SplashScreen = () => (
  <PhoneMockup title="1.1 Splash Screen" showNav={false}>
    <div style={{ height: "100%", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", padding: 24 }}>
      <TrakkrIcon size={100}/>
      <div style={{ marginTop: 16, fontSize: 10, color: COLORS.textDim, letterSpacing: 2 }}>LOADING...</div>
      <div style={{ width: 120, height: 3, background: COLORS.surfaceContainer, borderRadius: 2, marginTop: 8, overflow: "hidden" }}>
        <div style={{ width: "60%", height: "100%", background: `linear-gradient(90deg, ${COLORS.goldDark}, ${COLORS.gold})`, borderRadius: 2 }}/>
      </div>
    </div>
  </PhoneMockup>
);

const WelcomeScreen = () => (
  <PhoneMockup title="1.2 Welcome Carousel" showNav={false}>
    <div style={{ height: "100%", display: "flex", flexDirection: "column", padding: "40px 20px 20px" }}>
      <div style={{ flex: 1, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", textAlign: "center" }}>
        {/* Illustration placeholder */}
        <div style={{ width: 140, height: 140, borderRadius: 70, background: COLORS.goldSubtle, border: `2px solid ${COLORS.goldBorder}`, display: "flex", alignItems: "center", justifyContent: "center", marginBottom: 24 }}>
          <svg viewBox="0 0 48 48" width={64} height={64} fill="none" stroke={COLORS.gold} strokeWidth="2" strokeLinecap="round">
            <path d="M10 38 Q18 24 22 26 Q28 29 32 18 Q36 8 40 12"/>
            <circle cx="40" cy="12" r="3" fill={COLORS.gold}/>
          </svg>
        </div>
        <div style={{ fontSize: 22, fontWeight: 800, color: COLORS.text, marginBottom: 8, letterSpacing: -0.5 }}>Your Activity.<br/>Your Way.</div>
        <div style={{ fontSize: 13, color: COLORS.textDim, lineHeight: 1.6, maxWidth: 220 }}>Track runs, rides, walks & hikes with zero social pressure. Your data stays on your device.</div>
      </div>
      {/* Page dots */}
      <div style={{ display: "flex", justifyContent: "center", gap: 8, marginBottom: 20 }}>
        <div style={{ width: 24, height: 4, borderRadius: 2, background: COLORS.gold }}/>
        <div style={{ width: 8, height: 4, borderRadius: 2, background: COLORS.surfaceContainer }}/>
        <div style={{ width: 8, height: 4, borderRadius: 2, background: COLORS.surfaceContainer }}/>
      </div>
      <PrimaryButton>Get Started</PrimaryButton>
      <div style={{ textAlign: "center", marginTop: 12, fontSize: 12, color: COLORS.textDim }}>Already have an account? <span style={{ color: COLORS.gold, fontWeight: 600 }}>Sign in</span></div>
    </div>
  </PhoneMockup>
);

const PermissionScreen = () => (
  <PhoneMockup title="1.3 Location Permission" showNav={false}>
    <div style={{ height: "100%", display: "flex", flexDirection: "column", padding: "40px 20px 20px" }}>
      <div style={{ flex: 1, display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", textAlign: "center" }}>
        <div style={{ width: 100, height: 100, borderRadius: 50, background: COLORS.goldSubtle, border: `2px solid ${COLORS.goldBorder}`, display: "flex", alignItems: "center", justifyContent: "center", marginBottom: 24 }}>
          <svg viewBox="0 0 48 48" width={48} height={48} fill="none" stroke={COLORS.gold} strokeWidth="2.5" strokeLinecap="round">
            <path d="M24 4C16 4 10 10 10 18c0 12 14 26 14 26s14-14 14-26c0-8-6-14-14-14z"/>
            <circle cx="24" cy="18" r="5"/>
          </svg>
        </div>
        <div style={{ fontSize: 18, fontWeight: 700, color: COLORS.text, marginBottom: 8 }}>Location Access</div>
        <div style={{ fontSize: 12, color: COLORS.textDim, lineHeight: 1.6, maxWidth: 220, marginBottom: 24 }}>Trakkr needs location access to track your activities. GPS works offline — no internet needed.</div>
        {/* Permission details */}
        <Card style={{ width: "100%", textAlign: "left" }}>
          {[
            { icon: "🔒", text: "Your location is never shared" },
            { icon: "📱", text: "Only used during active tracking" },
            { icon: "💾", text: "Stored locally on your device" },
          ].map((item, i) => (
            <div key={i} style={{ display: "flex", alignItems: "center", gap: 8, padding: "6px 0" }}>
              <span style={{ fontSize: 14 }}>{item.icon}</span>
              <span style={{ fontSize: 11, color: COLORS.text }}>{item.text}</span>
            </div>
          ))}
        </Card>
      </div>
      <PrimaryButton>Allow Location</PrimaryButton>
      <div style={{ textAlign: "center", marginTop: 12, fontSize: 11, color: COLORS.textDim }}>You can change this in Settings anytime</div>
    </div>
  </PhoneMockup>
);

// ─── TRACK SCREENS ──────────────────────────────────────────

const PreTrackScreen = () => (
  <PhoneMockup title="2.1 Pre-Track (Home)" activeNav="track">
    <AppBar title="Trakkr" rightIcons={<><span>🔔</span><span>⚙️</span></>}/>
    {/* Quick stats banner */}
    <div style={{ padding: "0 12px 8px" }}>
      <Card>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 8 }}>
          <span style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, letterSpacing: 1 }}>THIS WEEK</span>
          <span style={{ fontSize: 10, color: COLORS.textDim }}>View all →</span>
        </div>
        <div style={{ display: "flex" }}>
          <StatBox label="DISTANCE" value="12.4" unit="km"/>
          <StatBox label="ACTIVITIES" value="3" unit=""/>
          <StatBox label="DURATION" value="1:24" unit="hrs"/>
        </div>
      </Card>
    </div>
    {/* Activity type selector */}
    <div style={{ padding: "0 12px 12px" }}>
      <SectionLabel>ACTIVITY TYPE</SectionLabel>
      <div style={{ display: "flex", gap: 6 }}>
        {ACTIVITY_TYPES.map((a, i) => (
          <div key={a.label} style={{
            flex: 1, display: "flex", flexDirection: "column", alignItems: "center", gap: 4,
            padding: "10px 4px 8px", borderRadius: 12,
            background: i === 0 ? COLORS.goldSubtle : COLORS.surface,
            border: `${i === 0 ? 2 : 1}px solid ${i === 0 ? COLORS.goldBorder : COLORS.outline}`,
          }}>
            <span style={{ fontSize: 20 }}>{a.icon}</span>
            <span style={{ fontSize: 9, color: i === 0 ? COLORS.gold : COLORS.textDim, fontWeight: 600 }}>{a.label}</span>
          </div>
        ))}
      </div>
    </div>
    {/* Mini map preview */}
    <div style={{ padding: "0 12px 12px" }}>
      <MapPlaceholder height={120} route={false} pin={true}/>
      <div style={{ fontSize: 9, color: COLORS.textDim, textAlign: "center", marginTop: 4 }}>📍 Cape Town, Western Cape</div>
    </div>
    {/* START button */}
    <div style={{ padding: "0 12px 12px" }}>
      <div style={{
        background: `linear-gradient(135deg, ${COLORS.goldDark}, ${COLORS.gold}, ${COLORS.goldLight})`,
        borderRadius: 20, padding: "18px 0", textAlign: "center",
        color: COLORS.bg, fontWeight: 800, fontSize: 20, letterSpacing: 2,
        boxShadow: `0 4px 20px rgba(225,165,0,0.3)`,
      }}>▶  START</div>
    </div>
    {/* Recent activity */}
    <div style={{ padding: "0 12px" }}>
      <SectionLabel>RECENT</SectionLabel>
      <ListItem icon="🏃" title="Morning Run" subtitle="Today · 5.2 km · 28:14" right="5'26″/km"/>
      <ListItem icon="🚴" title="Evening Ride" subtitle="Yesterday · 22.1 km · 48:30" right="27.3 km/h" border={false}/>
    </div>
  </PhoneMockup>
);

const ActiveTrackingScreen = () => (
  <PhoneMockup title="2.2 Active Tracking" activeNav="track">
    {/* Top: Map (split view) */}
    <MapPlaceholder height={220} route={true} live={true}/>
    {/* Floating activity badge */}
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "6px 12px", background: COLORS.surface, borderBottom: `1px solid ${COLORS.outline}` }}>
      <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
        <span style={{ fontSize: 14 }}>🏃</span>
        <span style={{ fontSize: 11, color: COLORS.gold, fontWeight: 700 }}>RUNNING</span>
        <span style={{ fontSize: 9, padding: "2px 6px", background: COLORS.successBg, color: COLORS.success, borderRadius: 4, fontWeight: 600 }}>● GPS</span>
      </div>
      <span style={{ fontSize: 10, color: COLORS.textDim }}>🔒</span>
    </div>
    {/* Stats panel */}
    <div style={{ padding: "12px 12px 8px" }}>
      {/* Primary stat: Duration */}
      <div style={{ textAlign: "center", marginBottom: 12 }}>
        <div style={{ fontSize: 42, fontWeight: 800, color: COLORS.text, fontFamily: "monospace", letterSpacing: 2 }}>28:14</div>
        <div style={{ fontSize: 10, color: COLORS.textDim, letterSpacing: 1 }}>DURATION</div>
      </div>
      {/* Secondary stats grid */}
      <div style={{ display: "flex", marginBottom: 12 }}>
        <StatBox label="DISTANCE" value="5.21" unit="km" large/>
        <div style={{ width: 1, background: COLORS.outline, margin: "0 4px" }}/>
        <StatBox label="PACE" value="5'26″" unit="/km" large/>
      </div>
      <div style={{ display: "flex", marginBottom: 12 }}>
        <StatBox label="ELEVATION" value="+42" unit="m"/>
        <div style={{ width: 1, background: COLORS.outline, margin: "0 4px" }}/>
        <StatBox label="CALORIES" value="312" unit="kcal"/>
        <div style={{ width: 1, background: COLORS.outline, margin: "0 4px" }}/>
        <StatBox label="AVG SPEED" value="11.1" unit="km/h"/>
      </div>
    </div>
    {/* Control buttons */}
    <div style={{ display: "flex", gap: 12, padding: "0 24px 12px", justifyContent: "center", alignItems: "center" }}>
      {/* Lap button */}
      <div style={{ width: 48, height: 48, borderRadius: 24, border: `2px solid ${COLORS.outline}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 11, color: COLORS.textDim, fontWeight: 600 }}>LAP</div>
      {/* Pause button */}
      <div style={{
        width: 64, height: 64, borderRadius: 32,
        background: `linear-gradient(135deg, ${COLORS.goldDark}, ${COLORS.gold})`,
        display: "flex", alignItems: "center", justifyContent: "center",
        boxShadow: `0 4px 16px rgba(225,165,0,0.4)`,
      }}>
        <svg viewBox="0 0 24 24" width={28} height={28} fill={COLORS.bg}>
          <rect x="6" y="4" width="4" height="16" rx="1"/><rect x="14" y="4" width="4" height="16" rx="1"/>
        </svg>
      </div>
      {/* Lock button */}
      <div style={{ width: 48, height: 48, borderRadius: 24, border: `2px solid ${COLORS.outline}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 16, color: COLORS.textDim }}>🔓</div>
    </div>
  </PhoneMockup>
);

const PausedScreen = () => (
  <PhoneMockup title="2.3 Tracking Paused" activeNav="track">
    <MapPlaceholder height={160} route={true}/>
    <div style={{ background: `${COLORS.gold}15`, borderBottom: `1px solid ${COLORS.goldBorder}`, padding: "8px 12px", display: "flex", alignItems: "center", justifyContent: "center", gap: 6 }}>
      <span style={{ fontSize: 11, color: COLORS.gold, fontWeight: 700, letterSpacing: 1 }}>⏸  PAUSED</span>
    </div>
    <div style={{ padding: "12px 12px 8px" }}>
      <div style={{ textAlign: "center", marginBottom: 12 }}>
        <div style={{ fontSize: 36, fontWeight: 800, color: COLORS.textDim, fontFamily: "monospace", letterSpacing: 2 }}>28:14</div>
      </div>
      <div style={{ display: "flex", marginBottom: 16 }}>
        <StatBox label="DISTANCE" value="5.21" unit="km"/>
        <StatBox label="PACE" value="5'26″" unit="/km"/>
        <StatBox label="ELEVATION" value="+42" unit="m"/>
      </div>
    </div>
    {/* Resume / Stop controls */}
    <div style={{ display: "flex", gap: 12, padding: "0 24px", justifyContent: "center", alignItems: "center" }}>
      {/* Stop button */}
      <div style={{ width: 56, height: 56, borderRadius: 28, border: `2px solid ${COLORS.error}`, display: "flex", alignItems: "center", justifyContent: "center" }}>
        <svg viewBox="0 0 24 24" width={24} height={24} fill={COLORS.error}>
          <rect x="6" y="6" width="12" height="12" rx="2"/>
        </svg>
      </div>
      {/* Resume button */}
      <div style={{
        width: 72, height: 72, borderRadius: 36,
        background: `linear-gradient(135deg, ${COLORS.goldDark}, ${COLORS.gold})`,
        display: "flex", alignItems: "center", justifyContent: "center",
        boxShadow: `0 4px 16px rgba(225,165,0,0.4)`,
      }}>
        <svg viewBox="0 0 24 24" width={32} height={32} fill={COLORS.bg}>
          <polygon points="8,4 20,12 8,20"/>
        </svg>
      </div>
      {/* Discard button */}
      <div style={{ width: 56, height: 56, borderRadius: 28, border: `2px solid ${COLORS.outline}`, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 11, color: COLORS.textDim, fontWeight: 600 }}>
        <span style={{ fontSize: 20 }}>🗑</span>
      </div>
    </div>
    <div style={{ textAlign: "center", marginTop: 16, padding: "0 12px" }}>
      <div style={{ display: "flex", gap: 8 }}>
        <div style={{ flex: 1 }}><OutlineButton>📤 Share Live</OutlineButton></div>
        <div style={{ flex: 1 }}><GhostButton>📸 Add Photo</GhostButton></div>
      </div>
    </div>
  </PhoneMockup>
);

const SaveScreen = () => (
  <PhoneMockup title="2.4 Save Activity" showNav={false}>
    <AppBar title="Save Activity" leftIcon="←" rightIcons={<span style={{ color: COLORS.gold, fontSize: 13, fontWeight: 700 }}>Save</span>}/>
    <MapPlaceholder height={140} route={true}/>
    {/* Stats summary */}
    <div style={{ display: "flex", padding: "10px 12px", background: COLORS.surface, borderBottom: `1px solid ${COLORS.outline}` }}>
      <StatBox label="DISTANCE" value="5.21" unit="km"/>
      <StatBox label="DURATION" value="28:14" unit=""/>
      <StatBox label="PACE" value="5'26″" unit="/km"/>
    </div>
    {/* Editable fields */}
    <div style={{ padding: "12px 12px" }}>
      {/* Title */}
      <div style={{ marginBottom: 12 }}>
        <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>TITLE</div>
        <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}` }}>
          <span style={{ fontSize: 14, color: COLORS.text }}>Morning Run</span>
        </div>
      </div>
      {/* Activity type */}
      <div style={{ marginBottom: 12 }}>
        <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>ACTIVITY TYPE</div>
        <div style={{ display: "flex", gap: 6 }}>
          {ACTIVITY_TYPES.slice(0, 4).map((a, i) => <Chip key={a.label} label={`${a.icon} ${a.label}`} active={i === 0}/>)}
        </div>
      </div>
      {/* Notes */}
      <div style={{ marginBottom: 12 }}>
        <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>NOTES</div>
        <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}`, minHeight: 48 }}>
          <span style={{ fontSize: 12, color: COLORS.textDim }}>Add notes about your activity...</span>
        </div>
      </div>
      {/* Weather & photos row */}
      <div style={{ display: "flex", gap: 8, marginBottom: 12 }}>
        <Card style={{ flex: 1, padding: 10, textAlign: "center" }}>
          <div style={{ fontSize: 20, marginBottom: 2 }}>☀️</div>
          <div style={{ fontSize: 10, color: COLORS.text }}>22°C</div>
          <div style={{ fontSize: 8, color: COLORS.textDim }}>Clear</div>
        </Card>
        <Card style={{ flex: 1, padding: 10, textAlign: "center" }}>
          <div style={{ fontSize: 20, marginBottom: 2 }}>📸</div>
          <div style={{ fontSize: 10, color: COLORS.textDim }}>Add photos</div>
        </Card>
      </div>
      <PrimaryButton>✓  Save Activity</PrimaryButton>
      <div style={{ textAlign: "center", marginTop: 10, fontSize: 11, color: COLORS.error }}>Discard Activity</div>
    </div>
  </PhoneMockup>
);

// ─── HISTORY SCREENS ────────────────────────────────────────

const HistoryScreen = () => (
  <PhoneMockup title="3.1 Activity History" activeNav="history">
    <AppBar title="History" rightIcons={<><span>🔍</span><span>📊</span></>}/>
    {/* Filter chips */}
    <div style={{ display: "flex", gap: 6, padding: "0 12px 10px", overflowX: "auto" }}>
      <Chip label="All" active/><Chip label="🏃 Run"/><Chip label="🚴 Cycle"/><Chip label="🚶 Walk"/><Chip label="🥾 Hike"/>
    </div>
    {/* Period selector */}
    <div style={{ display: "flex", padding: "0 12px 8px", gap: 4 }}>
      {["Week", "Month", "Year", "All"].map((p, i) => (
        <div key={p} style={{
          flex: 1, textAlign: "center", padding: "6px 0", borderRadius: 8, fontSize: 11, fontWeight: 600,
          background: i === 1 ? COLORS.gold : "transparent",
          color: i === 1 ? COLORS.bg : COLORS.textDim,
        }}>{p}</div>
      ))}
    </div>
    {/* Month summary card */}
    <div style={{ padding: "0 12px 10px" }}>
      <Card highlight>
        <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, letterSpacing: 1, marginBottom: 8 }}>MARCH 2026</div>
        <div style={{ display: "flex" }}>
          <StatBox label="DISTANCE" value="48.2" unit="km"/>
          <StatBox label="ACTIVITIES" value="12" unit=""/>
          <StatBox label="DURATION" value="5:42" unit="hrs"/>
        </div>
      </Card>
    </div>
    {/* Activity list */}
    <div style={{ padding: "0 12px" }}>
      <SectionLabel>TODAY</SectionLabel>
      <ListItem icon="🏃" title="Morning Run" subtitle="5.2 km · 28:14 · 5'26″/km" right={<span style={{ color: COLORS.gold }}>→</span>}/>
      <SectionLabel>YESTERDAY</SectionLabel>
      <ListItem icon="🚴" title="Evening Ride" subtitle="22.1 km · 48:30 · 27.3 km/h" right={<span style={{ color: COLORS.gold }}>→</span>}/>
      <ListItem icon="🚶" title="Lunch Walk" subtitle="2.8 km · 32:10 · 11'29″/km" right={<span style={{ color: COLORS.gold }}>→</span>}/>
      <SectionLabel>MONDAY</SectionLabel>
      <ListItem icon="🏃" title="Park Run" subtitle="5.0 km · 26:45 · 5'21″/km" right={<span style={{ color: COLORS.gold }}>→</span>} border={false}/>
    </div>
  </PhoneMockup>
);

const ActivityDetailScreen = () => (
  <PhoneMockup title="3.2 Activity Detail" showNav={false}>
    <AppBar title="Morning Run" leftIcon="←" rightIcons={<><span>✏️</span><span>📤</span><span>⋮</span></>}/>
    <MapPlaceholder height={180} route={true}/>
    {/* Stats grid */}
    <div style={{ padding: "12px" }}>
      <div style={{ display: "flex", marginBottom: 10 }}>
        <StatBox label="DISTANCE" value="5.21" unit="km" large/>
        <StatBox label="DURATION" value="28:14" unit="" large/>
        <StatBox label="AVG PACE" value="5'26″" unit="/km" large/>
      </div>
      <div style={{ display: "flex", marginBottom: 12 }}>
        <StatBox label="ELEVATION" value="+42" unit="m"/>
        <StatBox label="CALORIES" value="312" unit="kcal"/>
        <StatBox label="AVG SPEED" value="11.1" unit="km/h"/>
        <StatBox label="MAX PACE" value="4'48″" unit="/km"/>
      </div>
      {/* Splits */}
      <SectionLabel>SPLITS</SectionLabel>
      <Card style={{ padding: 10 }}>
        <div style={{ display: "flex", justifyContent: "space-between", padding: "0 4px 6px", borderBottom: `1px solid ${COLORS.outline}` }}>
          <span style={{ fontSize: 9, color: COLORS.textDim, width: 30 }}>KM</span>
          <span style={{ fontSize: 9, color: COLORS.textDim, flex: 1, textAlign: "center" }}>PACE</span>
          <span style={{ fontSize: 9, color: COLORS.textDim, flex: 1, textAlign: "center" }}>ELEV</span>
          <span style={{ fontSize: 9, color: COLORS.textDim, width: 40, textAlign: "right" }}></span>
        </div>
        {[
          { km: 1, pace: "5'32″", elev: "+8", bar: 85 },
          { km: 2, pace: "5'18″", elev: "+12", bar: 92 },
          { km: 3, pace: "5'45″", elev: "+15", bar: 78 },
          { km: 4, pace: "5'22″", elev: "+4", bar: 90 },
          { km: 5, pace: "5'17″", elev: "+3", bar: 94 },
        ].map(s => (
          <div key={s.km} style={{ display: "flex", alignItems: "center", padding: "5px 4px", borderBottom: `1px solid ${COLORS.outline}20` }}>
            <span style={{ fontSize: 11, color: COLORS.textDim, width: 30, fontWeight: 600 }}>{s.km}</span>
            <span style={{ fontSize: 11, color: COLORS.text, flex: 1, textAlign: "center", fontFamily: "monospace" }}>{s.pace}</span>
            <span style={{ fontSize: 10, color: COLORS.textDim, flex: 1, textAlign: "center" }}>{s.elev}m</span>
            <div style={{ width: 40, height: 6, background: COLORS.surfaceContainer, borderRadius: 3, overflow: "hidden" }}>
              <div style={{ width: `${s.bar}%`, height: "100%", background: COLORS.gold, borderRadius: 3 }}/>
            </div>
          </div>
        ))}
      </Card>
      {/* Notes */}
      <div style={{ marginTop: 10 }}>
        <SectionLabel>WEATHER</SectionLabel>
        <div style={{ display: "flex", gap: 8 }}>
          <Chip label="☀️ 22°C"/><Chip label="💨 12 km/h"/><Chip label="💧 45%"/>
        </div>
      </div>
    </div>
  </PhoneMockup>
);

const StatsScreen = () => (
  <PhoneMockup title="3.3 Statistics" activeNav="history">
    <AppBar title="Statistics" leftIcon="←"/>
    {/* Period tabs */}
    <div style={{ display: "flex", padding: "0 12px 8px", gap: 4 }}>
      {["W", "M", "Y", "All"].map((p, i) => (
        <div key={p} style={{
          flex: 1, textAlign: "center", padding: "6px 0", borderRadius: 8, fontSize: 11, fontWeight: 600,
          background: i === 1 ? COLORS.gold : "transparent",
          color: i === 1 ? COLORS.bg : COLORS.textDim,
        }}>{p}</div>
      ))}
    </div>
    {/* Chart placeholder */}
    <div style={{ padding: "0 12px 10px" }}>
      <Card>
        <div style={{ fontSize: 10, color: COLORS.textDim, marginBottom: 8 }}>DISTANCE (KM) — MARCH 2026</div>
        <div style={{ display: "flex", alignItems: "flex-end", gap: 3, height: 80, padding: "0 2px" }}>
          {[12, 0, 8, 5, 18, 0, 0, 6, 14, 0, 5, 0, 0, 10, 8, 0, 12, 6, 0, 0, 5, 22, 0, 8, 0, 14, 5, 0, 0].map((v, i) => (
            <div key={i} style={{
              flex: 1, height: `${Math.max(v * 3.5, 2)}%`,
              background: v > 0 ? (i === 21 ? COLORS.gold : `${COLORS.gold}60`) : COLORS.surfaceContainer,
              borderRadius: 2, minHeight: 2,
            }}/>
          ))}
        </div>
        <div style={{ display: "flex", justifyContent: "space-between", marginTop: 4 }}>
          <span style={{ fontSize: 8, color: COLORS.textDim }}>1</span>
          <span style={{ fontSize: 8, color: COLORS.textDim }}>15</span>
          <span style={{ fontSize: 8, color: COLORS.textDim }}>29</span>
        </div>
      </Card>
    </div>
    {/* Summary stats */}
    <div style={{ padding: "0 12px 10px" }}>
      <Card>
        <div style={{ display: "flex" }}>
          <StatBox label="TOTAL DIST" value="48.2" unit="km"/>
          <StatBox label="ACTIVITIES" value="12" unit=""/>
          <StatBox label="AVG PACE" value="5'31″" unit="/km"/>
        </div>
      </Card>
    </div>
    {/* Personal records */}
    <div style={{ padding: "0 12px" }}>
      <SectionLabel>PERSONAL RECORDS 🏆</SectionLabel>
      <Card style={{ padding: 10 }}>
        {[
          { label: "Fastest 5K", value: "24:15", date: "12 Mar 2026" },
          { label: "Longest Run", value: "12.3 km", date: "8 Mar 2026" },
          { label: "Longest Ride", value: "45.6 km", date: "22 Feb 2026" },
          { label: "Highest Climb", value: "+284 m", date: "1 Mar 2026" },
        ].map((r, i) => (
          <div key={r.label} style={{ display: "flex", alignItems: "center", padding: "6px 2px", borderBottom: i < 3 ? `1px solid ${COLORS.outline}30` : "none" }}>
            <span style={{ fontSize: 12, marginRight: 8 }}>🏅</span>
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 11, color: COLORS.text }}>{r.label}</div>
              <div style={{ fontSize: 9, color: COLORS.textDim }}>{r.date}</div>
            </div>
            <span style={{ fontSize: 13, color: COLORS.gold, fontWeight: 700, fontFamily: "monospace" }}>{r.value}</span>
          </div>
        ))}
      </Card>
    </div>
  </PhoneMockup>
);

// ─── ROUTE SCREENS ──────────────────────────────────────────

const RoutesListScreen = () => (
  <PhoneMockup title="4.1 Saved Routes" activeNav="routes">
    <AppBar title="Routes" rightIcons={<span style={{ fontSize: 13, color: COLORS.gold, fontWeight: 600 }}>+ New</span>}/>
    <div style={{ display: "flex", gap: 6, padding: "0 12px 10px" }}>
      <Chip label="My Routes" active/><Chip label="Imported"/><Chip label="Favourites ★"/>
    </div>
    <div style={{ padding: "0 12px" }}>
      {[
        { name: "Table Mountain Loop", dist: "8.4 km", elev: "+620m", type: "🥾" },
        { name: "Sea Point Promenade", dist: "12.2 km", elev: "+15m", type: "🏃" },
        { name: "Chapman's Peak Ride", dist: "32.8 km", elev: "+480m", type: "🚴" },
      ].map((r, i) => (
        <Card key={r.name} style={{ marginBottom: 8, padding: 0, overflow: "hidden" }}>
          <div style={{ height: 60, background: "#1a2a1a", position: "relative" }}>
            <svg style={{ position: "absolute", inset: 0 }} viewBox="0 0 280 60" preserveAspectRatio="none">
              <path d={["M20 45 Q60 20 100 30 Q160 45 220 15 Q250 5 265 20", "M15 30 Q80 40 140 25 Q200 10 265 35", "M20 50 Q80 15 140 35 Q200 55 260 20"][i]}
                fill="none" stroke={COLORS.gold} strokeWidth="2" strokeLinecap="round" opacity="0.7"/>
            </svg>
          </div>
          <div style={{ padding: "10px 12px", display: "flex", alignItems: "center", gap: 8 }}>
            <span style={{ fontSize: 18 }}>{r.type}</span>
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 13, color: COLORS.text, fontWeight: 600 }}>{r.name}</div>
              <div style={{ fontSize: 10, color: COLORS.textDim }}>{r.dist} · {r.elev}</div>
            </div>
            <span style={{ color: COLORS.gold }}>→</span>
          </div>
        </Card>
      ))}
    </div>
    {/* Import GPX */}
    <div style={{ padding: "8px 12px" }}>
      <OutlineButton>📂  Import GPX / KML</OutlineButton>
    </div>
  </PhoneMockup>
);

const RoutePlannerScreen = () => (
  <PhoneMockup title="4.2 Route Planner" showNav={false}>
    <AppBar title="Plan Route" leftIcon="←" rightIcons={<span style={{ color: COLORS.gold, fontSize: 13, fontWeight: 600 }}>Save</span>}/>
    {/* Full map with drawing tools */}
    <div style={{ position: "relative" }}>
      <MapPlaceholder height={300} route={true}/>
      {/* Drawing toolbar */}
      <div style={{ position: "absolute", left: 8, top: 8, display: "flex", flexDirection: "column", gap: 4 }}>
        {[
          { icon: "✏️", active: true },
          { icon: "↩️", active: false },
          { icon: "🗑", active: false },
        ].map((t, i) => (
          <div key={i} style={{
            width: 36, height: 36, borderRadius: 8,
            background: t.active ? COLORS.gold : "rgba(30,30,30,0.9)",
            display: "flex", alignItems: "center", justifyContent: "center", fontSize: 16,
            border: `1px solid ${t.active ? COLORS.gold : COLORS.outline}`,
          }}>{t.icon}</div>
        ))}
      </div>
      {/* Snap to road toggle */}
      <div style={{ position: "absolute", bottom: 8, left: 8, right: 8 }}>
        <div style={{ background: "rgba(30,30,30,0.9)", borderRadius: 10, padding: "8px 12px", display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <span style={{ fontSize: 11, color: COLORS.text }}>Snap to road</span>
          <div style={{ width: 36, height: 20, borderRadius: 10, background: COLORS.gold, position: "relative" }}>
            <div style={{ width: 16, height: 16, borderRadius: 8, background: COLORS.bg, position: "absolute", right: 2, top: 2 }}/>
          </div>
        </div>
      </div>
    </div>
    {/* Route info panel */}
    <div style={{ padding: "12px" }}>
      <div style={{ display: "flex", marginBottom: 12 }}>
        <StatBox label="DISTANCE" value="8.4" unit="km"/>
        <StatBox label="EST. TIME" value="52:00" unit=""/>
        <StatBox label="ELEVATION" value="+620" unit="m"/>
      </div>
      {/* Route name */}
      <div style={{ marginBottom: 10 }}>
        <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>ROUTE NAME</div>
        <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}` }}>
          <span style={{ fontSize: 13, color: COLORS.text }}>Table Mountain Loop</span>
        </div>
      </div>
      {/* Activity type */}
      <div style={{ display: "flex", gap: 6, marginBottom: 12 }}>
        {ACTIVITY_TYPES.slice(0, 4).map((a, i) => <Chip key={a.label} label={`${a.icon} ${a.label}`} active={i === 3}/>)}
      </div>
      <div style={{ display: "flex", gap: 8 }}>
        <div style={{ flex: 1 }}><PrimaryButton>💾  Save Route</PrimaryButton></div>
        <div style={{ flex: 1 }}><OutlineButton>📤 Export GPX</OutlineButton></div>
      </div>
    </div>
  </PhoneMockup>
);

// ─── LIVE SHARING SCREENS ───────────────────────────────────

const LiveShareSetupScreen = () => (
  <PhoneMockup title="5.1 Live Share Setup" showNav={false}>
    <AppBar title="Live Sharing" leftIcon="←"/>
    <div style={{ padding: "16px 12px", textAlign: "center" }}>
      <div style={{ width: 80, height: 80, borderRadius: 40, background: COLORS.goldSubtle, border: `2px solid ${COLORS.goldBorder}`, display: "inline-flex", alignItems: "center", justifyContent: "center", marginBottom: 16 }}>
        <svg viewBox="0 0 48 48" width={40} height={40} fill="none" stroke={COLORS.gold} strokeWidth="2.5" strokeLinecap="round">
          <path d="M24 4C16 4 10 10 10 18c0 12 14 26 14 26s14-14 14-26c0-8-6-14-14-14z"/>
          <circle cx="24" cy="18" r="4"/>
          <path d="M32 14 Q38 18 38 24" opacity="0.5"/><path d="M34 10 Q42 16 42 26" opacity="0.3"/>
        </svg>
      </div>
      <div style={{ fontSize: 16, fontWeight: 700, color: COLORS.text, marginBottom: 4 }}>Share Your Location</div>
      <div style={{ fontSize: 11, color: COLORS.textDim, lineHeight: 1.5, maxWidth: 220, margin: "0 auto" }}>Let friends and family follow your activity in real-time on a map.</div>
    </div>
    <div style={{ padding: "0 12px" }}>
      {/* Privacy settings */}
      <SectionLabel>PRIVACY SETTINGS</SectionLabel>
      <Card style={{ marginBottom: 12 }}>
        {[
          { label: "Anyone with the link", desc: "Generate a shareable URL", active: true },
          { label: "Selected contacts only", desc: "Choose who can see you", active: false },
        ].map((o, i) => (
          <div key={o.label} style={{ display: "flex", alignItems: "center", gap: 10, padding: "8px 0", borderBottom: i === 0 ? `1px solid ${COLORS.outline}` : "none" }}>
            <div style={{ width: 18, height: 18, borderRadius: 9, border: `2px solid ${o.active ? COLORS.gold : COLORS.outline}`, display: "flex", alignItems: "center", justifyContent: "center" }}>
              {o.active && <div style={{ width: 8, height: 8, borderRadius: 4, background: COLORS.gold }}/>}
            </div>
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 12, color: COLORS.text }}>{o.label}</div>
              <div style={{ fontSize: 9, color: COLORS.textDim }}>{o.desc}</div>
            </div>
          </div>
        ))}
      </Card>
      <SectionLabel>SHARE OPTIONS</SectionLabel>
      <Card style={{ marginBottom: 12 }}>
        {[
          { label: "Show pace/speed", on: true },
          { label: "Show distance", on: true },
          { label: "Show ETA", on: false },
        ].map((o, i) => (
          <div key={o.label} style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "8px 0", borderBottom: i < 2 ? `1px solid ${COLORS.outline}` : "none" }}>
            <span style={{ fontSize: 12, color: COLORS.text }}>{o.label}</span>
            <div style={{ width: 36, height: 20, borderRadius: 10, background: o.on ? COLORS.gold : COLORS.surfaceContainer, position: "relative" }}>
              <div style={{ width: 16, height: 16, borderRadius: 8, background: o.on ? COLORS.bg : COLORS.textDim, position: "absolute", top: 2, ...(o.on ? { right: 2 } : { left: 2 }) }}/>
            </div>
          </div>
        ))}
      </Card>
      <PrimaryButton>📡  Start Live Sharing</PrimaryButton>
    </div>
  </PhoneMockup>
);

const LiveShareActiveScreen = () => (
  <PhoneMockup title="5.2 Live Share Active" showNav={false}>
    <div style={{ background: COLORS.successBg, padding: "6px 12px", display: "flex", alignItems: "center", justifyContent: "center", gap: 6 }}>
      <span style={{ fontSize: 9, color: COLORS.success, fontWeight: 700, letterSpacing: 1 }}>● LIVE — 2 VIEWERS</span>
    </div>
    <MapPlaceholder height={200} route={true} live={true}/>
    <div style={{ padding: "10px 12px" }}>
      {/* Shareable link */}
      <Card highlight style={{ marginBottom: 10 }}>
        <div style={{ fontSize: 9, color: COLORS.gold, fontWeight: 700, letterSpacing: 1, marginBottom: 6 }}>SHARE LINK</div>
        <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
          <div style={{ flex: 1, fontSize: 10, color: COLORS.text, fontFamily: "monospace", overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>trakkr.app/live/abc-xyz-123</div>
          <div style={{ padding: "4px 10px", background: COLORS.gold, borderRadius: 6, fontSize: 10, fontWeight: 700, color: COLORS.bg }}>Copy</div>
        </div>
      </Card>
      {/* Viewers */}
      <SectionLabel>VIEWERS</SectionLabel>
      <Card style={{ padding: 10, marginBottom: 10 }}>
        <ListItem icon="👤" title="Sarah M." subtitle="Viewing now" right={<span style={{ color: COLORS.success, fontSize: 9 }}>● Live</span>} border={true}/>
        <ListItem icon="👤" title="Mom" subtitle="Viewing now" right={<span style={{ color: COLORS.success, fontSize: 9 }}>● Live</span>} border={false}/>
      </Card>
      {/* Live stats being shared */}
      <div style={{ display: "flex", marginBottom: 12 }}>
        <StatBox label="DISTANCE" value="3.2" unit="km"/>
        <StatBox label="PACE" value="5'18″" unit="/km"/>
        <StatBox label="DURATION" value="16:58" unit=""/>
      </div>
      <div style={{
        borderRadius: 12, padding: "11px 0", textAlign: "center",
        border: `1.5px solid ${COLORS.error}`,
        color: COLORS.error, fontWeight: 600, fontSize: 13,
      }}>⏹  Stop Sharing</div>
    </div>
  </PhoneMockup>
);

// ─── PROFILE & SETTINGS SCREENS ─────────────────────────────

const ProfileScreen = () => (
  <PhoneMockup title="6.1 Profile (Signed In)" activeNav="profile">
    <AppBar title="Profile" rightIcons={<span>⚙️</span>}/>
    <div style={{ padding: "8px 12px", textAlign: "center" }}>
      {/* Avatar */}
      <div style={{ width: 64, height: 64, borderRadius: 32, background: COLORS.goldSubtle, border: `2px solid ${COLORS.goldBorder}`, display: "inline-flex", alignItems: "center", justifyContent: "center", fontSize: 28 }}>JN</div>
      <div style={{ fontSize: 16, fontWeight: 700, color: COLORS.text, marginTop: 8 }}>Jaco N.</div>
      <div style={{ fontSize: 11, color: COLORS.textDim }}>Cape Town, South Africa</div>
      <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: 4, marginTop: 4 }}>
        <span style={{ fontSize: 9, padding: "2px 8px", background: COLORS.goldSubtle, color: COLORS.gold, borderRadius: 4, fontWeight: 600 }}>PRO</span>
        <span style={{ fontSize: 9, color: COLORS.success }}>● Synced</span>
      </div>
    </div>
    {/* All-time stats */}
    <div style={{ padding: "8px 12px" }}>
      <Card>
        <div style={{ fontSize: 9, color: COLORS.gold, fontWeight: 700, letterSpacing: 1, marginBottom: 8 }}>ALL-TIME STATS</div>
        <div style={{ display: "flex" }}>
          <StatBox label="DISTANCE" value="482" unit="km"/>
          <StatBox label="ACTIVITIES" value="96" unit=""/>
          <StatBox label="DURATION" value="48:20" unit="hrs"/>
        </div>
      </Card>
    </div>
    {/* Menu items */}
    <div style={{ padding: "0 12px" }}>
      {[
        { icon: "📊", label: "Statistics & Records", right: "→" },
        { icon: "🗺️", label: "Offline Maps", right: "2 regions →" },
        { icon: "📤", label: "Export Data", right: "→" },
        { icon: "🎯", label: "Goals & Challenges", right: "→" },
        { icon: "🔔", label: "Notifications", right: "→" },
        { icon: "⚙️", label: "Settings", right: "→" },
        { icon: "📋", label: "Privacy Policy", right: "→" },
        { icon: "ℹ️", label: "About Trakkr", right: "v1.0 →" },
      ].map((item, i) => (
        <ListItem key={item.label} icon={item.icon} title={item.label} right={<span style={{ color: COLORS.gold }}>{item.right}</span>} border={i < 7}/>
      ))}
    </div>
  </PhoneMockup>
);

const SettingsScreen = () => (
  <PhoneMockup title="6.2 Settings" showNav={false}>
    <AppBar title="Settings" leftIcon="←"/>
    <div style={{ padding: "0 12px" }}>
      <SectionLabel>TRACKING</SectionLabel>
      <Card style={{ marginBottom: 12, padding: 10 }}>
        {[
          { label: "Auto-pause", value: "On" },
          { label: "Auto-pause sensitivity", value: "Medium" },
          { label: "GPS accuracy", value: "High" },
          { label: "Screen always on", value: "Off" },
        ].map((s, i) => (
          <div key={s.label} style={{ display: "flex", justifyContent: "space-between", padding: "8px 2px", borderBottom: i < 3 ? `1px solid ${COLORS.outline}` : "none" }}>
            <span style={{ fontSize: 12, color: COLORS.text }}>{s.label}</span>
            <span style={{ fontSize: 11, color: COLORS.gold, fontWeight: 600 }}>{s.value}</span>
          </div>
        ))}
      </Card>
      <SectionLabel>DISPLAY</SectionLabel>
      <Card style={{ marginBottom: 12, padding: 10 }}>
        {[
          { label: "Units", value: "Metric (km)" },
          { label: "Theme", value: "Dark" },
          { label: "Map style", value: "Street" },
          { label: "Audio cues", value: "Every 1 km" },
        ].map((s, i) => (
          <div key={s.label} style={{ display: "flex", justifyContent: "space-between", padding: "8px 2px", borderBottom: i < 3 ? `1px solid ${COLORS.outline}` : "none" }}>
            <span style={{ fontSize: 12, color: COLORS.text }}>{s.label}</span>
            <span style={{ fontSize: 11, color: COLORS.gold, fontWeight: 600 }}>{s.value}</span>
          </div>
        ))}
      </Card>
      <SectionLabel>ACCOUNT</SectionLabel>
      <Card style={{ padding: 10 }}>
        {[
          { label: "Cloud sync", value: "On" },
          { label: "Sync on Wi-Fi only", value: "Off" },
          { label: "Sign out", value: "", color: COLORS.error },
        ].map((s, i) => (
          <div key={s.label} style={{ display: "flex", justifyContent: "space-between", padding: "8px 2px", borderBottom: i < 2 ? `1px solid ${COLORS.outline}` : "none" }}>
            <span style={{ fontSize: 12, color: s.color || COLORS.text }}>{s.label}</span>
            <span style={{ fontSize: 11, color: COLORS.gold, fontWeight: 600 }}>{s.value}</span>
          </div>
        ))}
      </Card>
    </div>
  </PhoneMockup>
);

const LoginScreen = () => (
  <PhoneMockup title="6.3 Login / Register" showNav={false}>
    <AppBar title="" leftIcon="←"/>
    <div style={{ padding: "16px 16px", display: "flex", flexDirection: "column", alignItems: "center" }}>
      <TrakkrIcon size={64}/>
      <div style={{ fontSize: 18, fontWeight: 700, color: COLORS.text, marginTop: 12, marginBottom: 4 }}>Welcome to Trakkr</div>
      <div style={{ fontSize: 11, color: COLORS.textDim, marginBottom: 20, textAlign: "center" }}>Sign in to sync your activities across devices. 100% optional.</div>
      {/* OAuth buttons */}
      <div style={{ width: "100%", display: "flex", flexDirection: "column", gap: 10, marginBottom: 16 }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: 10, padding: "12px 0", borderRadius: 12, background: "#FFFFFF", color: "#1F1F1F", fontWeight: 600, fontSize: 13 }}>
          <span style={{ fontSize: 16 }}>G</span> Continue with Google
        </div>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: 10, padding: "12px 0", borderRadius: 12, background: "#000000", color: "#FFFFFF", fontWeight: 600, fontSize: 13, border: `1px solid ${COLORS.outline}` }}>
          <span style={{ fontSize: 16 }}></span> Continue with Apple
        </div>
      </div>
      {/* Divider */}
      <div style={{ display: "flex", alignItems: "center", gap: 12, width: "100%", marginBottom: 16 }}>
        <div style={{ flex: 1, height: 1, background: COLORS.outline }}/>
        <span style={{ fontSize: 10, color: COLORS.textDim }}>or</span>
        <div style={{ flex: 1, height: 1, background: COLORS.outline }}/>
      </div>
      {/* Email fields */}
      <div style={{ width: "100%" }}>
        <div style={{ marginBottom: 10 }}>
          <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>EMAIL</div>
          <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}` }}>
            <span style={{ fontSize: 13, color: COLORS.textDim }}>your@email.com</span>
          </div>
        </div>
        <div style={{ marginBottom: 14 }}>
          <div style={{ fontSize: 9, color: COLORS.textDim, letterSpacing: 0.8, marginBottom: 4 }}>PASSWORD</div>
          <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}` }}>
            <span style={{ fontSize: 13, color: COLORS.textDim }}>••••••••</span>
          </div>
        </div>
        <PrimaryButton>Sign In</PrimaryButton>
        <div style={{ textAlign: "center", marginTop: 12, fontSize: 11, color: COLORS.textDim }}>
          Don't have an account? <span style={{ color: COLORS.gold, fontWeight: 600 }}>Register</span>
        </div>
        <div style={{ textAlign: "center", marginTop: 8, fontSize: 11, color: COLORS.gold }}>Forgot password?</div>
      </div>
    </div>
  </PhoneMockup>
);

const OfflineMapsScreen = () => (
  <PhoneMockup title="6.4 Offline Maps" showNav={false}>
    <AppBar title="Offline Maps" leftIcon="←" rightIcons={<span style={{ color: COLORS.gold, fontSize: 13, fontWeight: 600 }}>+ Add</span>}/>
    <div style={{ padding: "0 12px" }}>
      <Card style={{ marginBottom: 10 }}>
        <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 4 }}>
          <span style={{ fontSize: 11, color: COLORS.text, fontWeight: 600 }}>Storage used</span>
          <span style={{ fontSize: 11, color: COLORS.gold, fontFamily: "monospace" }}>324 MB</span>
        </div>
        <div style={{ height: 6, background: COLORS.surfaceContainer, borderRadius: 3, overflow: "hidden" }}>
          <div style={{ width: "32%", height: "100%", background: COLORS.gold, borderRadius: 3 }}/>
        </div>
      </Card>
      <SectionLabel>DOWNLOADED REGIONS</SectionLabel>
      {[
        { name: "Cape Town Metro", size: "186 MB", updated: "Updated 2 days ago", icon: "🗺️" },
        { name: "Table Mountain NP", size: "138 MB", updated: "Updated 1 week ago", icon: "🏔️" },
      ].map((r, i) => (
        <Card key={r.name} style={{ marginBottom: 8, padding: 10 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
            <span style={{ fontSize: 20 }}>{r.icon}</span>
            <div style={{ flex: 1 }}>
              <div style={{ fontSize: 13, color: COLORS.text, fontWeight: 600 }}>{r.name}</div>
              <div style={{ fontSize: 10, color: COLORS.textDim }}>{r.size} · {r.updated}</div>
            </div>
            <span style={{ fontSize: 14, color: COLORS.textDim }}>⋮</span>
          </div>
        </Card>
      ))}
      <div style={{ marginTop: 8 }}>
        <OutlineButton>🗺️  Browse Map to Download</OutlineButton>
      </div>
    </div>
  </PhoneMockup>
);

// ─── WIDGET SCREENS ─────────────────────────────────────────

const WidgetScreen = () => (
  <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 24 }}>
    <div style={{ color: COLORS.gold, fontWeight: 700, fontSize: 12, letterSpacing: 1.5, textTransform: "uppercase" }}>7. Android Home Screen Widgets</div>
    {/* Quick Start Widget (4x1) */}
    <div>
      <div style={{ fontSize: 10, color: COLORS.textDim, marginBottom: 6, textAlign: "center" }}>Quick Start Widget (4×1)</div>
      <div style={{ width: 280, background: COLORS.surface, borderRadius: 16, padding: "12px 14px", border: `1px solid ${COLORS.outline}`, display: "flex", alignItems: "center", gap: 10, boxShadow: "0 4px 16px rgba(0,0,0,0.4)" }}>
        <TrakkrIcon size={32}/>
        <div style={{ flex: 1 }}>
          <div style={{ fontSize: 12, fontWeight: 700, color: COLORS.text }}>Trakkr</div>
          <div style={{ fontSize: 9, color: COLORS.textDim }}>Ready to track</div>
        </div>
        <div style={{
          padding: "8px 16px", borderRadius: 10,
          background: `linear-gradient(135deg, ${COLORS.goldDark}, ${COLORS.gold})`,
          color: COLORS.bg, fontWeight: 700, fontSize: 11,
        }}>▶ START</div>
      </div>
    </div>
    {/* Weekly Stats Widget (4x2) */}
    <div>
      <div style={{ fontSize: 10, color: COLORS.textDim, marginBottom: 6, textAlign: "center" }}>Weekly Stats Widget (4×2)</div>
      <div style={{ width: 280, background: COLORS.surface, borderRadius: 16, padding: 14, border: `1px solid ${COLORS.outline}`, boxShadow: "0 4px 16px rgba(0,0,0,0.4)" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 10 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 6 }}>
            <TrakkrIcon size={20}/>
            <span style={{ fontSize: 11, fontWeight: 700, color: COLORS.text }}>This Week</span>
          </div>
          <span style={{ fontSize: 9, color: COLORS.textDim }}>Mar 24–30</span>
        </div>
        <div style={{ display: "flex", marginBottom: 10 }}>
          <StatBox label="DISTANCE" value="12.4" unit="km"/>
          <StatBox label="RUNS" value="3" unit=""/>
          <StatBox label="TIME" value="1:24" unit=""/>
        </div>
        {/* Mini bar chart */}
        <div style={{ display: "flex", alignItems: "flex-end", gap: 4, height: 32 }}>
          {["M", "T", "W", "T", "F", "S", "S"].map((d, i) => (
            <div key={d + i} style={{ flex: 1, textAlign: "center" }}>
              <div style={{
                height: [18, 0, 12, 0, 24, 8, 0][i],
                background: [18, 0, 12, 0, 24, 8, 0][i] > 0 ? COLORS.gold : COLORS.surfaceContainer,
                borderRadius: 2, minHeight: 2, marginBottom: 2,
              }}/>
              <span style={{ fontSize: 7, color: COLORS.textDim }}>{d}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
    {/* Activity Quick-Select Widget (2x2) */}
    <div>
      <div style={{ fontSize: 10, color: COLORS.textDim, marginBottom: 6, textAlign: "center" }}>Quick Select Widget (2×2)</div>
      <div style={{ width: 150, background: COLORS.surface, borderRadius: 16, padding: 12, border: `1px solid ${COLORS.outline}`, boxShadow: "0 4px 16px rgba(0,0,0,0.4)" }}>
        <div style={{ display: "flex", alignItems: "center", gap: 4, marginBottom: 8 }}>
          <TrakkrIcon size={16}/><span style={{ fontSize: 10, fontWeight: 700, color: COLORS.text }}>Quick Start</span>
        </div>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 6 }}>
          {ACTIVITY_TYPES.slice(0, 4).map(a => (
            <div key={a.label} style={{ background: COLORS.surfaceContainer, borderRadius: 8, padding: "8px 4px", textAlign: "center", border: `1px solid ${COLORS.outline}` }}>
              <span style={{ fontSize: 16 }}>{a.icon}</span>
              <div style={{ fontSize: 8, color: COLORS.textDim, marginTop: 2 }}>{a.label}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  </div>
);

// ─── MAIN VIEWER ────────────────────────────────────────────

const Section = ({ title, desc, children }) => (
  <div style={{ marginBottom: 48 }}>
    <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8 }}>
      <div style={{ width: 4, height: 28, background: COLORS.gold, borderRadius: 2 }}/>
      <h2 style={{ margin: 0, fontSize: 22, fontWeight: 700, color: COLORS.text, letterSpacing: -0.5 }}>{title}</h2>
    </div>
    {desc && <div style={{ fontSize: 12, color: COLORS.textDim, marginBottom: 20, lineHeight: 1.6, paddingLeft: 16 }}>{desc}</div>}
    {children}
  </div>
);

const ScreenRow = ({ children }) => (
  <div style={{ display: "flex", flexWrap: "wrap", gap: 24, justifyContent: "center" }}>{children}</div>
);

export default function TrakkrWireframes() {
  const [activeTab, setActiveTab] = useState("track");
  const tabs = [
    { id: "onboarding", label: "Onboarding" },
    { id: "track", label: "Track Flow" },
    { id: "history", label: "History & Stats" },
    { id: "routes", label: "Routes" },
    { id: "live", label: "Live Sharing" },
    { id: "profile", label: "Profile & Settings" },
    { id: "widgets", label: "Widgets" },
    { id: "system", label: "Design System" },
  ];

  return (
    <div style={{ background: "#0A0A0A", minHeight: "100vh", color: COLORS.text, fontFamily: "'Inter', 'Roboto', system-ui, sans-serif" }}>
      {/* Header */}
      <div style={{ padding: "32px 24px 16px", borderBottom: `1px solid ${COLORS.outline}` }}>
        <div style={{ display: "flex", alignItems: "center", gap: 16, marginBottom: 8 }}>
          <TrakkrIcon size={56}/>
          <div>
            <h1 style={{ margin: 0, fontSize: 28, fontWeight: 800, color: COLORS.text, letterSpacing: -1 }}>Trakkr</h1>
            <div style={{ fontSize: 12, color: COLORS.gold, fontWeight: 600, letterSpacing: 2, textTransform: "uppercase" }}>UI/UX Wireframes & Screen Specs • v1.0</div>
          </div>
        </div>
        <div style={{ fontSize: 11, color: COLORS.textDim, marginTop: 4, marginBottom: 12 }}>
          Comprehensive screen designs for the Trakkr activity tracking app. Dark theme (#121212) with gold (#E1A500) accent — NovoTech brand system.
        </div>
        <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
          {tabs.map(t => (
            <button key={t.id} onClick={() => setActiveTab(t.id)} style={{
              padding: "8px 14px", borderRadius: 20, border: "none", cursor: "pointer",
              background: activeTab === t.id ? COLORS.gold : COLORS.surfaceContainer,
              color: activeTab === t.id ? COLORS.bg : COLORS.textDim,
              fontWeight: 600, fontSize: 11, transition: "all 0.2s",
            }}>{t.label}</button>
          ))}
        </div>
      </div>

      {/* Content */}
      <div style={{ padding: "24px 24px 48px", maxWidth: 1400, margin: "0 auto" }}>

        {activeTab === "onboarding" && (
          <Section title="1. Onboarding Flow" desc="First-launch experience. Splash → Welcome carousel (3 slides) → Location permission. No forced account creation — app works immediately after granting location.">
            <ScreenRow>
              <SplashScreen/>
              <WelcomeScreen/>
              <PermissionScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "track" && (
          <Section title="2. Track Activity Flow" desc="Core user journey: Pre-track (select type + start) → Active tracking (split view: map top, stats bottom) → Pause → Save. The START button is the hero element on the home screen.">
            <ScreenRow>
              <PreTrackScreen/>
              <ActiveTrackingScreen/>
              <PausedScreen/>
              <SaveScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "history" && (
          <Section title="3. History & Statistics" desc="Activity feed with search/filter, detailed activity view with splits, statistics dashboard with charts and personal records.">
            <ScreenRow>
              <HistoryScreen/>
              <ActivityDetailScreen/>
              <StatsScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "routes" && (
          <Section title="4. Routes & Planning" desc="Saved routes library, draw-on-map route planner with snap-to-road, GPX import/export. Route planning uses GraphHopper for snap-to-road.">
            <ScreenRow>
              <RoutesListScreen/>
              <RoutePlannerScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "live" && (
          <Section title="5. Live Location Sharing" desc="Share real-time location and stats with selected contacts or via link. Uses Supabase Realtime for GPS broadcasting. Privacy-first: user controls who sees them and can stop anytime.">
            <ScreenRow>
              <LiveShareSetupScreen/>
              <LiveShareActiveScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "profile" && (
          <Section title="6. Profile & Settings" desc="Optional account with OAuth login. Settings for tracking, display, and sync preferences. Offline map management. App works 100% without an account.">
            <ScreenRow>
              <ProfileScreen/>
              <LoginScreen/>
              <SettingsScreen/>
              <OfflineMapsScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "widgets" && (
          <Section title="7. Android Home Screen Widgets" desc="Three widget sizes for quick access: Quick Start (4×1), Weekly Stats (4×2), and Activity Quick Select (2×2). All deep-link into the app.">
            <ScreenRow>
              <WidgetScreen/>
            </ScreenRow>
          </Section>
        )}

        {activeTab === "system" && (
          <Section title="Design System Reference" desc="Complete design token reference for the NovoTech brand system adapted for Trakkr.">
            {/* Colors */}
            <div style={{ marginBottom: 24 }}>
              <div style={{ fontSize: 14, fontWeight: 700, color: COLORS.text, marginBottom: 10 }}>Brand Colors</div>
              <div style={{ display: "flex", flexWrap: "wrap", gap: 8 }}>
                {[
                  { name: "Primary Gold", hex: "#E1A500" },
                  { name: "Gold Light", hex: "#F0C030" },
                  { name: "Gold Dark", hex: "#C89000" },
                  { name: "Background", hex: "#121212" },
                  { name: "Surface", hex: "#1E1E1E" },
                  { name: "Surface Container", hex: "#2A2A2A" },
                  { name: "Text Primary", hex: "#E8E2D6" },
                  { name: "Text Dim", hex: "#9E9A92" },
                  { name: "Success", hex: "#4ADE80" },
                  { name: "Error", hex: "#FFB4AB" },
                  { name: "Info", hex: "#60A5FA" },
                  { name: "Outline", hex: "#3A3A3A" },
                ].map(c => (
                  <div key={c.name} style={{ display: "flex", alignItems: "center", gap: 8, padding: 8, background: COLORS.surface, borderRadius: 8, border: `1px solid ${COLORS.outline}` }}>
                    <div style={{ width: 28, height: 28, borderRadius: 6, background: c.hex, border: `1px solid ${COLORS.outline}` }}/>
                    <div>
                      <div style={{ fontSize: 10, color: COLORS.text, fontWeight: 600 }}>{c.name}</div>
                      <div style={{ fontSize: 9, color: COLORS.textDim, fontFamily: "monospace" }}>{c.hex}</div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
            {/* Typography */}
            <div style={{ marginBottom: 24 }}>
              <div style={{ fontSize: 14, fontWeight: 700, color: COLORS.text, marginBottom: 10 }}>Typography Scale</div>
              <Card>
                {[
                  { label: "H1 / Hero Stat", size: "42px", weight: "800", font: "Monospace", sample: "28:14" },
                  { label: "H2 / Section Title", size: "22px", weight: "700", font: "System", sample: "Activity History" },
                  { label: "H3 / Card Title", size: "17px", weight: "700", font: "System", sample: "Morning Run" },
                  { label: "Body", size: "13–14px", weight: "500", font: "System", sample: "5.2 km · 28:14 · 5'26″/km" },
                  { label: "Caption", size: "11px", weight: "400", font: "System", sample: "Updated 2 days ago" },
                  { label: "Label / Overline", size: "9px", weight: "700", font: "System", sample: "THIS WEEK" },
                ].map((t, i) => (
                  <div key={t.label} style={{ display: "flex", alignItems: "baseline", gap: 12, padding: "8px 4px", borderBottom: i < 5 ? `1px solid ${COLORS.outline}` : "none" }}>
                    <div style={{ width: 120 }}>
                      <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 600 }}>{t.label}</div>
                      <div style={{ fontSize: 8, color: COLORS.textDim }}>{t.size} / {t.weight} / {t.font}</div>
                    </div>
                    <span style={{ fontSize: parseInt(t.size), fontWeight: parseInt(t.weight), color: COLORS.text, fontFamily: t.font === "Monospace" ? "monospace" : "inherit" }}>{t.sample}</span>
                  </div>
                ))}
              </Card>
            </div>
            {/* Spacing & Radii */}
            <div style={{ marginBottom: 24 }}>
              <div style={{ fontSize: 14, fontWeight: 700, color: COLORS.text, marginBottom: 10 }}>Spacing & Radii</div>
              <Card>
                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 8, padding: 4 }}>
                  {[
                    { label: "Screen padding", value: "12–16px" },
                    { label: "Card padding", value: "14px" },
                    { label: "Card radius", value: "14px" },
                    { label: "Button radius", value: "12–14px" },
                    { label: "Chip radius", value: "20px (pill)" },
                    { label: "Input radius", value: "10px" },
                    { label: "FAB radius", value: "50% (circle)" },
                    { label: "List item height", value: "48px min" },
                    { label: "Bottom nav height", value: "56px" },
                    { label: "App bar height", value: "48px" },
                  ].map(s => (
                    <div key={s.label} style={{ display: "flex", justifyContent: "space-between", padding: "4px 0" }}>
                      <span style={{ fontSize: 10, color: COLORS.textDim }}>{s.label}</span>
                      <span style={{ fontSize: 10, color: COLORS.text, fontFamily: "monospace" }}>{s.value}</span>
                    </div>
                  ))}
                </div>
              </Card>
            </div>
            {/* Components */}
            <div>
              <div style={{ fontSize: 14, fontWeight: 700, color: COLORS.text, marginBottom: 10 }}>Component Library</div>
              <div style={{ display: "flex", flexWrap: "wrap", gap: 12 }}>
                <Card style={{ padding: 12, minWidth: 200 }}>
                  <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, marginBottom: 8 }}>BUTTONS</div>
                  <div style={{ display: "flex", flexDirection: "column", gap: 8 }}>
                    <div style={{ background: COLORS.gold, borderRadius: 12, padding: "10px 16px", textAlign: "center", color: COLORS.bg, fontWeight: 700, fontSize: 12 }}>Primary Button</div>
                    <div style={{ borderRadius: 12, padding: "10px 16px", textAlign: "center", border: `1.5px solid ${COLORS.gold}`, color: COLORS.gold, fontWeight: 600, fontSize: 12 }}>Outline Button</div>
                    <div style={{ borderRadius: 12, padding: "10px 16px", textAlign: "center", background: COLORS.surfaceContainer, color: COLORS.text, fontSize: 12 }}>Ghost Button</div>
                    <div style={{ borderRadius: 12, padding: "10px 16px", textAlign: "center", border: `1.5px solid ${COLORS.error}`, color: COLORS.error, fontWeight: 600, fontSize: 12 }}>Destructive</div>
                  </div>
                </Card>
                <Card style={{ padding: 12, minWidth: 200 }}>
                  <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, marginBottom: 8 }}>CHIPS</div>
                  <div style={{ display: "flex", flexWrap: "wrap", gap: 6 }}>
                    <Chip label="Active Chip" active/><Chip label="Inactive Chip"/><Chip label="🏃 With Icon" active/>
                  </div>
                  <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, marginBottom: 8, marginTop: 12 }}>STATUS BADGES</div>
                  <div style={{ display: "flex", gap: 6, flexWrap: "wrap" }}>
                    <span style={{ fontSize: 9, padding: "3px 8px", background: COLORS.successBg, color: COLORS.success, borderRadius: 4, fontWeight: 600 }}>● GPS</span>
                    <span style={{ fontSize: 9, padding: "3px 8px", background: COLORS.goldSubtle, color: COLORS.gold, borderRadius: 4, fontWeight: 600 }}>PRO</span>
                    <span style={{ fontSize: 9, padding: "3px 8px", background: COLORS.errorBg, color: COLORS.error, borderRadius: 4, fontWeight: 600 }}>● PAUSED</span>
                    <span style={{ fontSize: 9, padding: "3px 8px", background: COLORS.infoBg, color: COLORS.info, borderRadius: 4, fontWeight: 600 }}>● LIVE</span>
                  </div>
                </Card>
                <Card style={{ padding: 12, minWidth: 200 }}>
                  <div style={{ fontSize: 10, color: COLORS.gold, fontWeight: 700, marginBottom: 8 }}>INPUTS</div>
                  <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.outline}`, marginBottom: 8 }}>
                    <span style={{ fontSize: 12, color: COLORS.textDim }}>Placeholder text</span>
                  </div>
                  <div style={{ background: COLORS.surfaceContainer, borderRadius: 10, padding: "10px 12px", border: `1px solid ${COLORS.gold}` }}>
                    <span style={{ fontSize: 12, color: COLORS.text }}>Active input</span>
                  </div>
                </Card>
              </div>
            </div>
          </Section>
        )}
      </div>
    </div>
  );
}
